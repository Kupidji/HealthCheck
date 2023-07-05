package com.example.healthcheck.view

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentStepsBinding
import com.example.healthcheck.model.steps.entities.Steps
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.ButtonPress.buttonPress
import com.example.healthcheck.util.animations.ProgressBarAnimation.animateProgressBar
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.model.steps.viewmodel.StepsViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class StepsFragment : Fragment() {

    companion object {
        fun newInstance() = StepsFragment()
    }

    private lateinit var viewModel: StepsViewModel
    private lateinit var binding : FragmentStepsBinding

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

        val currentDate = Calendar.getInstance().timeInMillis
        var id = 0
        var day = 0L

        //Востанавливает max для progressbar для недели и месяца
        changeMaxOfProgressBar()

        //Востанавливает progress для ProgressBar для недели и месяца
        changeProgressBar()

        //Подгрузка данных
        viewModel.currentGoal.observe(this@StepsFragment.viewLifecycleOwner) {
            loadData(it)
        }

        //Записывает количество шагов за день в EditText и обновляет калории
        viewModel.totalStepsForDay.observe(this@StepsFragment.viewLifecycleOwner) {
            if (it != 0) {
                binding.getCountOfSteps.setText(it.toString())
            }
            binding.dayCal.text = kKAL(it).toString() + " калорий"
        }

        viewModel.day.observe(this@StepsFragment.viewLifecycleOwner) {
            if (it != null) {
                day = it
            }
        }

        viewModel.id.observe(this@StepsFragment.viewLifecycleOwner) {
            if (it != null) {
                id = it
            }
        }

        //Каждый день обновляет поле шагов
        viewModel.day.observe(this@StepsFragment.viewLifecycleOwner) {
            if ((SimpleDateFormat("dd").format(it)) != (SimpleDateFormat("dd").format(currentDate))) {
                binding.getCountOfSteps.setText("")
                saveDataForCountOfStepsOfDay(0)
                viewModel.setCurrentStepsForDay(0)
            }
        }

        //Калории за неделю
        viewModel.totalStepsForWeek.observe(this@StepsFragment.viewLifecycleOwner) {
            binding.weekCal.text = it?.let { it1 -> kKAL(it1).toString() } + " калорий"
        }

        //Калории за месяц
        viewModel.totalStepsForMonth.observe(this@StepsFragment.viewLifecycleOwner) {
            binding.monthCal.text = it?.let { it1 -> kKAL(it1).toString() } + " калорий"
        }

        binding.wentBack.setOnClickListener {
            if (binding.getCountOfSteps.text.isNotEmpty()) {
                if (binding.getCountOfSteps.text.toString().toInt() in 1..200000) {
                    saveOrUpdateStepBd()
                }
            }
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

        //Если фокус уйдет
        binding.getCountOfSteps.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && binding.getCountOfSteps.text.isNotEmpty()) {
                saveAfterKeyboardClosedOrLostFocusForSteps(id, day)
            }
        }
        binding.customTarget.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && binding.customTarget.text.isNotEmpty()) {
                saveAfterKeyboardClosedOrLostFocusForTarget()
            }
            else if(!hasFocus && binding.customTarget.text.isEmpty() && viewModel.currentGoal.value != 5000 && viewModel.currentGoal.value != 10000 && viewModel.currentGoal.value != 15000) {
                viewModel.setCurrentTarget(10000)
                saveDataForTarget(10000)
                loadData(10000)
                changeProgressBar()
                changeMaxOfProgressBar()
            }
        }

        //Если клавиатура убрана, а поля были заполнены новыми данными и фокус был на них, то
        //Сохраняет цель и количество шагов за день в SharedPref, меняет их во viewModel, загружает
        //В базу данных количество шагов и обновляет max и progress для progressBar
        view.viewTreeObserver.addOnGlobalLayoutListener {
            var r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            var heightDiff = view.rootView.height - r.height()

            //Если клавиатура убрана
            if (heightDiff < 0.2 * view.rootView.height) {

                binding.getCountOfSteps.isCursorVisible = false
                binding.customTarget.isCursorVisible = false

                if (binding.getCountOfSteps.text.isNotEmpty() && binding.getCountOfSteps.isFocused) {
                    saveAfterKeyboardClosedOrLostFocusForSteps(id, day)
                }
                if (binding.customTarget.text.isNotEmpty() && binding.customTarget.isFocused) {
                    saveAfterKeyboardClosedOrLostFocusForTarget()
                }
                else if(binding.customTarget.isFocused && binding.customTarget.text.isEmpty() && viewModel.currentGoal.value != 5000 && viewModel.currentGoal.value != 10000 && viewModel.currentGoal.value != 15000) {
                    viewModel.setCurrentTarget(10000)
                    saveDataForTarget(10000)
                    loadData(10000)
                    changeProgressBar()
                    changeMaxOfProgressBar()
                }

            }
            else {
                binding.getCountOfSteps.isCursorVisible = true
                binding.customTarget.isCursorVisible = true
            }

        }

        //По нажатию кнопки делает ее выбранной, меняет текущую цель и Progress на 5000
        binding.st5000.setOnClickListener {
            buttonPress(binding.st5000)
            viewModel.setCurrentTarget(5000)
            loadData(5000)
            changeProgressBar()
            saveDataForTarget(5000)
        }

        //По нажатию кнопки делает ее выбранной, меняет текущую цель и Progress на 10000
        binding.st10000.setOnClickListener {
            buttonPress(binding.st10000)
            viewModel.setCurrentTarget(10000)
            loadData(10000)
            changeProgressBar()
            saveDataForTarget(10000)
        }

        //По нажатию кнопки делает ее выбранной, меняет текущую цель и Progress на 15000
        binding.st15000.setOnClickListener {
            buttonPress(binding.st15000)
            viewModel.setCurrentTarget(15000)
            loadData(15000)
            changeProgressBar()
            saveDataForTarget(15000)
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.getCountOfSteps.text.isNotEmpty()) {
                    if (binding.getCountOfSteps.text.toString().toInt() in 1..200000) {
                        saveOrUpdateStepBd()
                    }
                }
                var navOptions = NavOptions.Builder()
                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                    .setPopUpTo(R.id.mainFragment, true)
                    .build()
                val direction = StepsFragmentDirections.actionStepsFragmentToMainFragment()
                navigation.navigate(direction, navOptions)
            }
        })

    }

    //Сохраняет и изменяет введеную информацию для количества шагов
    private fun saveAfterKeyboardClosedOrLostFocusForSteps(id: Int, day: Long) {
        if (binding.getCountOfSteps.text.isNotEmpty()) {
            if (binding.getCountOfSteps.text.toString().toInt() in 1..200000) {

                if (binding.getCountOfSteps.text.toString().toInt() != viewModel.settings.getInt(
                        Constants.STEPS_PER_DAY,
                        0
                    )
                ) {

                    //Сохраняет количество шагов в SharedPref
                    saveDataForCountOfStepsOfDay(binding.getCountOfSteps.text.toString().toInt())

                    //Обновляет количесто шагов в viewModel
                    viewModel.setCurrentStepsForDay(binding.getCountOfSteps.text.toString().toInt())

                    //Сохраняет или обновляет базу данных
                    saveOrUpdateStepBdKeyBoard(id, day)

                    //Обновляет количество шагов за неделю и за месяц и id date последней записи, так как обновилась база данных
                    viewModel.setCurrentStepsForWeek()
                    viewModel.setCurrentStepsForMonth()
                    viewModel.setCurrentDate()
                    viewModel.setCurrentId()

                    binding.getCountOfSteps.setSelection(binding.getCountOfSteps.text.toString().length)

                }

                binding.getCountOfSteps.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            } else {
                binding.getCountOfSteps.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.error_ic,
                    0
                )
            }
        }
    }

    //Сохраняет и изменяет введеную информацию для цели
    private fun saveAfterKeyboardClosedOrLostFocusForTarget() {

        if (binding.customTarget.text.toString().toInt() in 1..1000000)
        {
            if (binding.customTarget.text.toString().toInt() != viewModel.settings.getInt(Constants.TARGET, 10000)) {

                //Сохраняет цель в SharedPref
                saveDataForTarget(binding.customTarget.text.toString().toInt())

                //Обновляет цель во ViewModel
                viewModel.setCurrentTarget(binding.customTarget.text.toString().toInt())

                //Меняет progreeBar
                changeMaxOfProgressBar()
                changeProgressBar()

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

    //Экран закрыт -> сохраняет цель и количество шагов за день
    override fun onDestroyView() {
        super.onDestroyView()
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

    //Сохранение цели в SharedPref
    private fun saveDataForTarget(target : Int) {

        val editor = viewModel.settings.edit()
        editor?.putInt(
            Constants.TARGET,
            target
        )?.apply()

    }

    //Сохранение количества шагов за день в SharedPred
    private fun saveDataForCountOfStepsOfDay(steps : Int) {

        val editor = viewModel.settings.edit()
        editor?.putInt(
            Constants.STEPS_PER_DAY,
            steps
        )?.apply()

    }

    //Меняет прогресс progressBar для месяца и недели
    private fun changeProgressBar() {

        viewModel.totalStepsForWeek.observe(this@StepsFragment.viewLifecycleOwner) {
            binding.countOfStepsForWeekText.setText("${it}")
            if (it != null) {
                animateProgressBar(binding.stepsDiagram, it)
                //binding.stepsDiagram.progress = it
            }
        }
        viewModel.totalStepsForMonth.observe(this@StepsFragment.viewLifecycleOwner) {
            binding.countOfStepsForMonthText.setText("${it}")
            if (it != null) {
                animateProgressBar(binding.stepsDiagram1, it)
                //binding.stepsDiagram1.progress = it
            }
        }

    }

    //Меняет максимально значение progressBars за неделю и месяц
    private fun changeMaxOfProgressBar() {

        viewModel.currentGoal.observe(this@StepsFragment.viewLifecycleOwner) {
            binding.stepsDiagram.max = it*7
            binding.stepsDiagram1.max = it*30
        }

    }

    //Загружает цель -> меняет кнопки и поле кастомной цели(если имеется)
    private fun loadData(res : Int) {

        if (res == 5000){
            changeButtonUI( binding.st5000)
        }
        else if (res == 10000){
            changeButtonUI( binding.st10000)
        }
        else if (res == 15000){
            changeButtonUI( binding.st15000)
        }
        else {
            changeButtonUI(binding.customTarget)
            binding.customTarget.setText(res.toString())
        }

    }

    //Формула для подсчета калорий
    private fun kKAL(Steps: Int): Int {
        return ((Steps.toFloat()/1300) * 0.52 * viewModel.settingsWeight.getFloat(Constants.WEIGHT_FOR_WEEK, 0F)).toInt()
    }

    //Сохраняет или обновляет базу данных
    //Если текущая дата есть в таблице -> обновляет количество шагов
    //Если нет -> вставляет
    private fun saveOrUpdateStepBd() {

        var id = 0
        var day = 0L

        viewModel.day.observe(this@StepsFragment.viewLifecycleOwner) {
            if (it != null) {
                day = it
            }
        }

        viewModel.id.observe(this@StepsFragment.viewLifecycleOwner) {
            if (it != null) {
                id = it
            }
        }

        saveOrUpdateStepBdKeyBoard(id, day)

    }

    private fun saveOrUpdateStepBdKeyBoard(id: Int,day: Long) {

        var currentDate = Calendar.getInstance().timeInMillis

        Log.d("date", "current date: $currentDate day: $day id : $id ")
        //Если новая дата не совпадает со старой -> insert
        if (SimpleDateFormat("dd.MM").format(currentDate) != SimpleDateFormat("dd.MM").format(day)) {

            if (binding.getCountOfSteps.text.isNotEmpty()) {

                var ourSteps = forStepBdValue(currentDate, 0)
                viewModel.insertSteps(ourSteps)

            }

        } else { //Если новая дата совпадает со старой -> update

            if (binding.getCountOfSteps.text.isNotEmpty()) {

                var ourSteps = forStepBdValue(currentDate, id)
                viewModel.updateSteps(ourSteps)

            }

        }

    }

    //Заполняет поле для обнвление или вставки в базу данных
    private fun forStepBdValue(currentDate: Long, id: Int): Steps {

        return Steps(
            id = id,
            countOfSteps = binding.getCountOfSteps.text.toString().toInt(),
            date = currentDate,
        )
    }

}

