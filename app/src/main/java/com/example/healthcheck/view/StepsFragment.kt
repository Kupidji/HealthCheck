package com.example.healthcheck.view

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentStepsBinding
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.steps.entities.Steps
import com.example.healthcheck.util.Constants
import com.example.healthcheck.viewmodel.StepsViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
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
    private lateinit var settings : SharedPreferences

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

        settings = requireActivity().applicationContext.getSharedPreferences("targetPref", Context.MODE_PRIVATE)

        //Подгрузка значения(5000,10000,150000)
        var save : Int = settings.getInt(Constants.TARGET, 10000)

        //Изменение кнопки на выбраную
        loadData(save)

        val tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())
        val navigation = findNavController()

        //viewModel.getStepsFromDataForWeek()
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            binding.countOfStepsForWeekText.setText("${viewModel.getStepsFromDataForWeek(tripletsPool)}")
        }


        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            binding.countOfStepsForMonthText.setText("${viewModel.getStepsFromDataForMonth(tripletsPool)}")
        }


        binding.wentBack.setOnClickListener {
            if (binding.getCountOfSteps.text.isNotEmpty()) {
                var currentDate = SimpleDateFormat("dd.MM", Locale.getDefault()).format(Date())
                var ourSteps = Steps (
                    countOfSteps = binding.getCountOfSteps.text.toString().toInt(),
                    date = currentDate,
                )
                viewModel.insertSteps(ourSteps)
            }
            navigation.navigate(R.id.mainFragment)
        }

        binding.profile.setOnClickListener {
            navigation.navigate(R.id.profileFragment)
        }

        binding.st5000.setOnClickListener {
            changeButton( binding.st5000)
            saveData(5000)
        }

        binding.st10000.setOnClickListener {
            changeButton(binding.st10000)
            saveData(10000)
        }

        binding.st15000.setOnClickListener {
            changeButton(binding.st15000)
            saveData(15000)
        }

    }

    private fun changeButton(view : View) {

        when (view) {

            binding.st5000 -> {
                setChoosenBtn(binding.st5000Text, binding.st5000)
                setUnchoosenBtn(binding.st10000Text, binding.st10000)
                setUnchoosenBtn(binding.st15000Text, binding.st15000)
            }

            binding.st10000 -> {
                setUnchoosenBtn(binding.st5000Text, binding.st5000)
                setChoosenBtn(binding.st10000Text, binding.st10000)
                setUnchoosenBtn(binding.st15000Text, binding.st15000)
            }

            binding.st15000 -> {
                setUnchoosenBtn(binding.st5000Text, binding.st5000)
                setUnchoosenBtn(binding.st10000Text, binding.st10000)
                setChoosenBtn(binding.st15000Text, binding.st15000)
            }

        }

    }

    private fun setChoosenBtn(view : TextView, background : View) {
        background.visibility = View.VISIBLE
        view.setTextColor(ContextCompat.getColor(requireContext(), R.color.textcolor_choosen))
    }

    private fun setUnchoosenBtn(view : TextView, background : View) {
        background.visibility = View.GONE
        view.setTextColor(ContextCompat.getColor(requireContext(), R.color.textcolor_unchoosen))
    }

    //Сохранение значения цели
    private fun saveData(res : Int) {

        val editor = settings.edit()
        editor?.putInt(Constants.TARGET, res)
        editor?.apply()

    }

    //Изменение кнопки
    private fun loadData(res : Int) {

        if (res == 5000){
            changeButton( binding.st5000)
        }
        if (res == 10000){
            changeButton( binding.st10000)
        }
        if (res == 15000){
            changeButton( binding.st15000)

        }

    }

}

