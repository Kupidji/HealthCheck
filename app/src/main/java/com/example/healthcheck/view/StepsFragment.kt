package com.example.healthcheck.view

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentStepsBinding
import com.example.healthcheck.model.steps.entities.Steps
import com.example.healthcheck.util.Constants
import com.example.healthcheck.viewmodel.StepsViewModel
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

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        var currentDate = Calendar.getInstance().timeInMillis

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
            binding.dayCal.text = kKAL(85.1, it).toString() + " калорий"
        }

        viewModel.day.observe(this@StepsFragment.viewLifecycleOwner) {
            if ((SimpleDateFormat("dd").format(it)) != (SimpleDateFormat("dd").format(currentDate))) {
                binding.getCountOfSteps.setText("")
            }
            saveDataForCountOfStepsOfDay(0)
        }

        //Калории за неделю
        viewModel.totalStepsForWeek.observe(this@StepsFragment.viewLifecycleOwner) {
            binding.weekCal.text = it?.let { it1 -> kKAL(85.1, it1).toString() } + " калорий"
        }

        //Калории за месяц
        viewModel.totalStepsForMonth.observe(this@StepsFragment.viewLifecycleOwner) {
            binding.monthCal.text = it?.let { it1 -> kKAL(85.1, it1).toString() } + " калорий"
        }

        binding.wentBack.setOnClickListener {
            val direction = StepsFragmentDirections.actionStepsFragmentToProfileFragment()
            navigation.navigate(direction, navOptions)
        }

        //Переход на профиль
        binding.profile.setOnClickListener {
            val direction = StepsFragmentDirections.actionStepsFragmentToProfileFragment()
            navigation.navigate(direction, navOptions)
        }

        //Если клавиатура убрана, а поля были заполнены новыми данными и фокус был на них, то
        //Сохраняет цель и количество шагов за день в SharedPref, меняет их во viewModel, загружает
        //В базу данных количество шагов и обновляет max и progress для progressBar
        view.viewTreeObserver.addOnGlobalLayoutListener {
            var r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            var heightDiff = view.rootView.height - r.height()

            //TODO Когда пишешь цель которая на кнопке, то первый раз стирает, второй раз нет
            //TODO Если при одной открытой клавиатуре записать и шаги и цель, то обновиться только то что последнее
            //Если клавиатура убрана
            if (heightDiff < 0.2 * view.rootView.height) {

                if (binding.getCountOfSteps.text.isNotEmpty() &&
                    binding.getCountOfSteps.text.toString().toInt() != viewModel.settings.getInt(Constants.STEPS_PER_DAY,0) &&
                        binding.getCountOfSteps.isFocused) {

                    //Сохраняет количество шагов в SharedPref
                    saveDataForCountOfStepsOfDay(binding.getCountOfSteps.text.toString().toInt())

                    //Обновляет количесто шагов в viewModel
                    viewModel.setCurrentStepsForDay(binding.getCountOfSteps.text.toString().toInt())

                    //Сохраняет или обновляет базу данных
                    saveOrUpdateStepBd()

                    //Обновляет количество шагов за неделю и за месяц и id date последней записи, так как обновилась база данных
                    viewModel.setCurrentStepsForWeek()
                    viewModel.setCurrentStepsForMonth()
                    viewModel.setCurrentDate()
                    viewModel.setCurrentId()

                    //Убирает фокус
                    binding.getCountOfSteps.clearFocus()

                }

                if (binding.customTarget.text.isNotEmpty() &&
                    binding.customTarget.text.toString().toInt() != viewModel.settings.getInt(Constants.TARGET,10000) &&
                        binding.customTarget.isFocused) {

                    //Сохраняет цель в SharedPref
                    saveDataForTarget(binding.customTarget.text.toString().toInt())

                    //Обновляет цель во ViewModel
                    viewModel.setCurrentTarget(binding.customTarget.text.toString().toInt())

                    //Меняет progreeBar
                    changeMaxOfProgressBar()
                    changeProgressBar()

                    //Убирает фокус
                    binding.customTarget.clearFocus()
                }

            }

        }

        //По нажатию кнопки делает ее выбранной, меняет текущую цель и Progress на 5000
        binding.st5000.setOnClickListener {
            viewModel.setCurrentTarget(5000)
            loadData(5000)
            changeProgressBar()
        }

        //По нажатию кнопки делает ее выбранной, меняет текущую цель и Progress на 10000
        binding.st10000.setOnClickListener {
            viewModel.setCurrentTarget(10000)
            loadData(10000)
            changeProgressBar()
        }

        //По нажатию кнопки делает ее выбранной, меняет текущую цель и Progress на 15000
        binding.st15000.setOnClickListener {
            viewModel.setCurrentTarget(15000)
            loadData(15000)
            changeProgressBar()
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
        viewModel.currentGoal.value?.let { saveDataForTarget(it) }
        viewModel.totalStepsForDay.value?.let { saveDataForCountOfStepsOfDay(it) }
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
                binding.stepsDiagram.progress = it
            }
        }
        viewModel.totalStepsForMonth.observe(this@StepsFragment.viewLifecycleOwner) {
            binding.countOfStepsForMonthText.setText("${it}")
            if (it != null) {
                binding.stepsDiagram1.progress = it
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
    private fun kKAL(Mass: Double, Steps: Int): Int {
        return (1.15 * Mass * Steps * 80 / 100000).toInt()
    }

    //Сохраняет или обновляет базу данных
    //Если текущая дата есть в таблице -> обновляет количество шагов
    //Если нет -> вставляет
    private fun saveOrUpdateStepBd() {

        var day = 0L
        var id = 0
        var currentDate = Date().time

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

        //Если новая дата не совпадает со старой -> insert
        if (SimpleDateFormat("dd.MM").format(currentDate) != SimpleDateFormat("dd.MM").format(day)) {

            if (binding.getCountOfSteps.text.isNotEmpty()) {

                var ourSteps = forStepBd(currentDate, 0)

                viewModel.insertSteps(ourSteps)

            }

        } else { //Если новая дата совпадает со старой -> update

            if (binding.getCountOfSteps.text.isNotEmpty()) {

                var ourSteps = forStepBd(currentDate, id)

                viewModel.updateSteps(ourSteps)

            }

        }

    }

    //Заполняет поле для обнвление или вставки в базу данных
    private fun forStepBd(currentDate : Long, id : Int) : Steps {

        var ourSteps = Steps(
            id = id,
            countOfSteps = binding.getCountOfSteps.text.toString().toInt(),
            date = currentDate,
        )

        saveDataForCountOfStepsOfDay(binding.getCountOfSteps.text.toString().toInt())

        return ourSteps
    }

}

