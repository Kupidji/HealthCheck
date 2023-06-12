package com.example.healthcheck.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentStepsBinding
import com.example.healthcheck.model.steps.entities.Steps
import com.example.healthcheck.util.Constants
import com.example.healthcheck.viewmodel.StepsViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class StepsFragment : Fragment() {

    companion object {
        fun newInstance() = StepsFragment()
    }

    private lateinit var viewModel: StepsViewModel
    private lateinit var binding : FragmentStepsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(StepsViewModel::class.java)
        binding = FragmentStepsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Получение цели из sharedPref
        var save : Int = viewModel.settings.getInt(Constants.TARGET, 10000)

        //Загрзука
        loadData(save)

        val navigation = findNavController()

        //viewModel.getStepsFromDataForWeek()
        viewModel.totalStepsForWeek.observe(this@StepsFragment.viewLifecycleOwner) {
            binding.countOfStepsForWeekText.setText("${it}")
        }
        viewModel.totalStepsForWeek.observe(this@StepsFragment.viewLifecycleOwner) {
            if (it != null) {
                binding.stepsDiagram.progress = it
            }
        }

        viewModel.totalStepsForMonth.observe(this@StepsFragment.viewLifecycleOwner) {
            binding.countOfStepsForMonthText.setText("${it}")
        }
        viewModel.totalStepsForMonth.observe(this@StepsFragment.viewLifecycleOwner) {
            if (it != null) {
                binding.stepsDiagram1.progress = it
            }
        }

//        viewModel.setTotalStepsForDay(binding.getCountOfSteps.toString().toInt())

        binding.wentBack.setOnClickListener {
            if (binding.getCountOfSteps.text.isNotEmpty()) {
                var currentDate = SimpleDateFormat("dd.MM", Locale.getDefault()).format(Date())
                var ourSteps = Steps (
                    countOfSteps = binding.getCountOfSteps.text.toString().toInt(),
                    date = currentDate,
                )
                viewModel.insertSteps(ourSteps)
                var editorForStepsPerDay = viewModel.settings.edit()
                editorForStepsPerDay?.putInt(Constants.STEPS_PER_DAY, binding.getCountOfSteps.text.toString().toInt())?.apply()
            }
            if (binding.customTarget.text.isNotEmpty()) {
                var editorForTarget = viewModel.settings.edit()
                editorForTarget?.putInt(Constants.TARGET, binding.customTarget.text.toString().toInt())?.apply()
            }
            navigation.navigate(R.id.mainFragment)
        }

        binding.profile.setOnClickListener {
            navigation.navigate(R.id.profileFragment)
        }

        binding.st5000.setOnClickListener {
            changeButton( binding.st5000)
            saveData(5000)
            loadData(5000)
        }

        binding.st10000.setOnClickListener {
            changeButton(binding.st10000)
            saveData(10000)
            loadData(10000)
        }

        binding.st15000.setOnClickListener {
            changeButton(binding.st15000)
            saveData(15000)
            loadData(15000)
        }
    }

    private fun changeButton(view : View) {

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

    private fun setChoosenBtn(view : TextView, background : ImageView) {
        background.setImageResource(R.drawable.sleep_box)
        view.setTextColor(ContextCompat.getColor(requireContext(), R.color.textcolor_choosen))
    }

    private fun setUnchoosenBtn(view : TextView, background : ImageView) {
        background.setImageResource(R.drawable.box)
        view.setTextColor(ContextCompat.getColor(requireContext(), R.color.textcolor_unchoosen))
    }

    //Сохранение цели
    private fun saveData(res : Int) {

        val editor = viewModel.settings.edit()
        editor?.putInt(Constants.TARGET, res)
        editor?.apply()

    }

    //Загрузка
    private fun loadData(res : Int) {

        if (res == 5000){
            changeButton( binding.st5000)
        }
        else if (res == 10000){
            changeButton( binding.st10000)
        }
        else if (res == 15000){
            changeButton( binding.st15000)
        }
        else {
            changeButton(binding.customTarget)
            binding.customTarget.setText(res.toString())
        }
// TODO Дождаться веса и заменить его
        binding.stepsDiagram.max = res*7
        binding.stepsDiagram1.max = res*30
        binding.dayCal.text = kKAL(85.1, viewModel.settings.getInt(Constants.STEPS_PER_DAY, 0)).toString() + " калорий"
        viewModel.totalStepsForWeek.observe(this@StepsFragment.viewLifecycleOwner) {
            binding.weekCal.text = it?.let { it1 -> kKAL(85.1, it1).toString() } + " калорий"
        }
        viewModel.totalStepsForMonth.observe(this@StepsFragment.viewLifecycleOwner) {
            binding.monthCal.text = it?.let { it1 -> kKAL(85.1, it1).toString() } + " калорий"
        }

    }

    private fun kKAL(Mass: Double, Steps: Int): Int {
        return (1.15 * Mass * Steps * 80 / 100000).toInt()
    }

}

