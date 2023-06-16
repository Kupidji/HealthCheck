package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.viewmodel.MainFragment1ViewModel
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentMain1Binding
import com.example.healthcheck.util.Constants
import java.text.SimpleDateFormat
import kotlin.math.abs

class mainFragment1 : Fragment() {

    companion object {
        fun newInstance() = mainFragment1()
    }

    private lateinit var viewModel: MainFragment1ViewModel
    private lateinit var binding : FragmentMain1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MainFragment1ViewModel::class.java)
        binding = FragmentMain1Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        binding.main1CountOfStepsDay.text = viewModel.settings.getInt(Constants.STEPS_PER_DAY, 0).toString()
        binding.progressBarSteps.max = viewModel.settings.getInt(Constants.TARGET, 10000)
        binding.progressBarSteps.progress = viewModel.settings.getInt(Constants.STEPS_PER_DAY, 0)

        binding.sleepHoursDay.text = SimpleDateFormat("HH:mm").format(viewModel.settingsForSleep.getLong(Constants.TIME_SLEEP, 0L) - 18000000) + "ч"

        binding.stepsBox.setOnClickListener {
            navigation.navigate(R.id.stepsFragment)
        }

        binding.sleepBox.setOnClickListener {
            navigation.navigate(R.id.sleepFragment)
        }

        binding.kardioBox.setOnClickListener {
            navigation.navigate(R.id.heartFragment)
        }

        binding.weightBox.setOnClickListener {
            navigation.navigate(R.id.weightFragment)
        }

    }

}