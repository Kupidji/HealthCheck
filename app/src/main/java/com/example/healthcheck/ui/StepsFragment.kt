package com.example.healthcheck.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.domain.AppDispatchers
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentStepsBinding
import com.example.healthcheck.util.animations.ButtonPress.buttonPressAnimation
import com.example.healthcheck.util.animations.ProgressBarAnimation.animateProgressBar
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.StepsViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class StepsFragment : Fragment() {

    companion object {
        fun newInstance() = StepsFragment()
    }

    private lateinit var viewModel: StepsViewModel
    private lateinit var binding : FragmentStepsBinding
    private var _currentTarget = 10000
    private var _totalStepsForDay = 0
    private var _day = 0L
    private var _id = 0
    private var _countOfStepsWeek = 0
    private var _countOfStepsMonth = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(StepsViewModel::class.java)
        binding = FragmentStepsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        //Подгрузка данных, востанавливает max у progressbar для недели и месяца
        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.currentTarget.collect { target ->
                restoreTarget(target = target)
                changeMaxOfProgressBarsSteps(target = target)
                if (_countOfStepsWeek != 0) {
                    changeProgressBarSteps(progressBar = binding.stepsDiagramForWeek, countOfSteps = _countOfStepsWeek)
                }
                if (_countOfStepsMonth != 0) {
                    changeProgressBarSteps(progressBar = binding.stepsDiagramForMonth, countOfSteps = _countOfStepsMonth)
                }
                _currentTarget = target
            }
        }

        //Прогресс у progressbar, количество шагов для недели, рассчитываются калории
        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.totalStepsForWeek.collect { countOfSteps ->
                binding.countOfStepsForWeekText.text = "$countOfSteps"
                _countOfStepsWeek = countOfSteps
                changeProgressBarSteps(progressBar = binding.stepsDiagramForWeek, countOfSteps = countOfSteps)
                viewModel.getKkalForWeek(steps =  countOfSteps)
            }
        }

        //Прогресс у progressbar, количество шагов для месяца, рассчитываются калории
        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.totalStepsForMonth.collect { countOfSteps ->
                binding.countOfStepsForMonthText.text = "$countOfSteps"
                _countOfStepsMonth = countOfSteps
                changeProgressBarSteps(progressBar = binding.stepsDiagramForMonth, countOfSteps = countOfSteps)
                viewModel.getKkalForMonth(steps = countOfSteps)
            }
        }

        //Шаги за день. Восстанавливается значение в editText, рассчитываются калории
        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.totalStepsForDay.collect { countOfSteps ->
                if (countOfSteps != 0) {
                    binding.getCountOfSteps.setText("$countOfSteps")
                }
                else {
                    binding.getCountOfSteps.setText("")
                }

                viewModel.getKkalForDay(countOfSteps)
                _totalStepsForDay = countOfSteps
            }
        }

        //Вывод калории за день
        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.kkalForDay.collect { kkal ->
                binding.dayCal.text = "$kkal " + context?.getString(R.string.kkal)
            }
        }

        //Вывод калории за неделю
        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.kkalForWeek.collect { kkal ->
                binding.weekCal.text = "$kkal " + context?.getString(R.string.kkal)
            }
        }

        //Вывод калории за месяц
        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.kkalForMonth.collect { kkal ->
                binding.monthCal.text = "$kkal " + context?.getString(R.string.kkal)
            }
        }

        //Для обновления данных шагов за день
        lifecycleScope.launch {
            viewModel.day.collect {
                if (it != null) {
                    _day = it
                }
            }
        }

        //Для обновления данных шагов за день
        lifecycleScope.launch {
            viewModel.id.collect {
                if (it != null) {
                    _id = it
                }
            }
        }

        //Если фокус уйдет
        binding.getCountOfSteps.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.getCountOfSteps.text.isNotEmpty()) {
                saveAfterKeyboardClosedOrLostFocusForSteps()
            }
        }

        binding.customTarget.setOnFocusChangeListener { _, hasFocus ->
            lifecycleScope.launch {
                viewModel.currentTarget.collect { value ->
                    if (!hasFocus && binding.customTarget.text.isNotEmpty()) {
                        saveAfterKeyboardClosedOrLostFocusForTarget()
                    }
                    else if(!hasFocus && binding.customTarget.text.isEmpty() && value != 5000 && value != 10000 && value != 15000) {
                        viewModel.setTarget(10000)
                    }
                }
            }
        }

        //Если клавиатура убрана, а поля были заполнены новыми данными и фокус был на них, то
        //Сохраняет цель и количество шагов за день в SharedPref, меняет их во viewModel, загружает
        //В базу данных количество шагов
        var keyboardStatus = false
        view.viewTreeObserver.addOnGlobalLayoutListener {
            var r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            var heightDiff = view.rootView.height - r.height()


            //Если клавиатура убрана
            if (heightDiff < 0.2 * view.rootView.height) {
                binding.getCountOfSteps.isCursorVisible = false
                binding.customTarget.isCursorVisible = false

                if (!keyboardStatus) {
                    keyboardStatus = true
                    if (binding.getCountOfSteps.text.isNotEmpty() && binding.getCountOfSteps.isFocused) {
                        saveAfterKeyboardClosedOrLostFocusForSteps()
                    }

                    if (binding.customTarget.text.isNotEmpty() && binding.customTarget.isFocused) {
                        saveAfterKeyboardClosedOrLostFocusForTarget()
                    }
                    else if (binding.customTarget.isFocused && binding.customTarget.text.isEmpty() && _currentTarget != 5000 && _currentTarget != 10000 && _currentTarget != 15000) {
                        viewModel.setTarget(10000)
                    }
                }

            }
            else {
                binding.getCountOfSteps.isCursorVisible = true
                binding.customTarget.isCursorVisible = true
                keyboardStatus = false
            }

        }

        //По нажатию кнопки делает ее выбранной, меняет текущую цель и Progress на 5000
        binding.st5000.setOnClickListener {
            buttonPressAnimation(binding.st5000)
            viewModel.setTarget(5000)
        }

        //По нажатию кнопки делает ее выбранной, меняет текущую цель и Progress на 10000
        binding.st10000.setOnClickListener {
            buttonPressAnimation(binding.st10000)
            viewModel.setTarget(10000)
        }

        //По нажатию кнопки делает ее выбранной, меняет текущую цель и Progress на 15000
        binding.st15000.setOnClickListener {
            buttonPressAnimation(binding.st15000)
            viewModel.setTarget(15000)
        }

        binding.wentBack.setOnClickListener {
            //навигация и анимации
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.mainFragment, true)
                .build()
            val direction = StepsFragmentDirections.actionStepsFragmentToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

    }

    //Сохраняет и изменяет введеную информацию для количества шагов
    private fun saveAfterKeyboardClosedOrLostFocusForSteps() {
        if (binding.getCountOfSteps.text.toString().toInt() in 1..200000) {
            if (binding.getCountOfSteps.text.toString().toInt() != _totalStepsForDay) {
                //Сохраняет или обновляет базу данных
                saveOrUpdateStepBdKeyBoard()
                binding.getCountOfSteps.setSelection(binding.getCountOfSteps.text.toString().length)

            }
            binding.getCountOfSteps.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
        else {
            binding.getCountOfSteps.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.error_ic,
                0
            )
        }
    }

    //Сохраняет и изменяет введеную информацию для цели
    private fun saveAfterKeyboardClosedOrLostFocusForTarget() {
        if (binding.customTarget.text.toString().toInt() in 1..1000000) {
            if (binding.customTarget.text.toString().toInt() != _currentTarget) {
                //Сохраняет цель
                viewModel.setTarget(target = binding.customTarget.text.toString().toInt())

                binding.customTarget.setSelection(binding.customTarget.text.toString().length)
            }
            binding.customTarget.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0)
        }
        else {
            binding.customTarget.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.error_ic, 0)
        }

    }

    //Меняет кнопки 5000,10000,15000
    private fun changeButtonUI(view : View) {

        when (view) {

            binding.st5000 -> {
                setChoosenBtn(binding.st5000Text, binding.st5000)
                setUnchoosenBtn(binding.st10000Text, binding.st10000)
                setUnchoosenBtn(binding.st15000Text, binding.st15000)
                binding.customTarget.setText("")
            }

            binding.st10000 -> {
                setUnchoosenBtn(binding.st5000Text, binding.st5000)
                setChoosenBtn(binding.st10000Text, binding.st10000)
                setUnchoosenBtn(binding.st15000Text, binding.st15000)
                binding.customTarget.setText("")
            }

            binding.st15000 -> {
                setUnchoosenBtn(binding.st5000Text, binding.st5000)
                setUnchoosenBtn(binding.st10000Text, binding.st10000)
                setChoosenBtn(binding.st15000Text, binding.st15000)
                binding.customTarget.setText("")
            }

            binding.customTarget -> {
                setUnchoosenBtn(binding.st5000Text, binding.st5000)
                setUnchoosenBtn(binding.st10000Text, binding.st10000)
                setUnchoosenBtn(binding.st15000Text, binding.st15000)
            }

        }

    }

    //Выбранная кнопка
    private fun setChoosenBtn(view : TextView, background : ImageView) {
        background.setImageResource(R.drawable.sleep_box)
        view.setTextColor(ContextCompat.getColor(requireContext(), R.color.textcolor_choosen))
    }

    //Невыбранная кнопка
    private fun setUnchoosenBtn(view : TextView, background : ImageView) {
        background.setImageResource(R.drawable.box)
        view.setTextColor(ContextCompat.getColor(requireContext(), R.color.textcolor_unchoosen))
    }

    //Меняет прогресс progressBar для недели
    private fun changeProgressBarSteps(progressBar: ProgressBar, countOfSteps : Int) {
        if (countOfSteps != null) {
            animateProgressBar(progressBar, countOfSteps)
        }
    }

    //Меняет максимально значение progressBars за неделю и месяц
    private fun changeMaxOfProgressBarsSteps(target : Int) {
        binding.stepsDiagramForWeek.max = target * 7
        binding.stepsDiagramForMonth.max = target * 30
    }

    //Загружает цель -> меняет кнопки и поле кастомной цели(если имеется)
    private fun restoreTarget(target : Int) {
        when(target) {
            5000 -> {
                changeButtonUI( binding.st5000)
            }

            10000 -> {
                changeButtonUI( binding.st10000)
            }

            15000 -> {
                changeButtonUI( binding.st15000)
            }

            else -> {
                binding.customTarget.setText(target.toString())
            }

        }

    }

    //Сохраняет или обновляет базу данных
    //Если текущая дата есть в таблице -> обновляет количество шагов
    //Если нет -> вставляет
    private fun saveOrUpdateStepBdKeyBoard() {
        var currentDate = Calendar.getInstance().timeInMillis
        if (SimpleDateFormat("dd.MM").format(currentDate) != SimpleDateFormat("dd.MM").format(_day)) {
            if (binding.getCountOfSteps.text.isNotEmpty()) {
                viewModel.insertSteps(steps = binding.getCountOfSteps.text.toString().toInt())
            }
        }
        else {
            if (binding.getCountOfSteps.text.isNotEmpty()) { //Если новая дата совпадает со старой -> update
                viewModel.updateSteps(id = _id, steps = binding.getCountOfSteps.text.toString().toInt())
            }
        }

    }

}