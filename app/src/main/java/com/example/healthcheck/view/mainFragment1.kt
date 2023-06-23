package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
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

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()


        binding.main1CountOfStepsDay.text = viewModel.settings.getInt(Constants.STEPS_PER_DAY, 0).toString()
        binding.progressBarSteps.max = viewModel.settings.getInt(Constants.TARGET, 10000)
        binding.progressBarSteps.progress = viewModel.settings.getInt(Constants.STEPS_PER_DAY, 0)

        var sleepy = SimpleDateFormat("HH:mm").format(viewModel.settingsForSleep.getLong(Constants.TIME_SLEEP, 0L))
        binding.sleepHoursDay.text = sleepy.split(":")[0].toInt().toString() + ":" + sleepy.split(":")[1] + "ч"

        binding.progressBarWeight.max = 120
        binding.progressBarWeight.progress = viewModel.settingsForWeight.getFloat(Constants.WEIGHT_FOR_DAY, 0F).toInt()
        binding.weightCountText.text = String.format("%.1f",viewModel.settingsForWeight.getFloat(Constants.WEIGHT_FOR_DAY, 0F))

        binding.stepsBox.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToStepsFragment()
            navigation.navigate(direction, navOptions)
        }

        binding.sleepBox.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToSleepFragment()
            navigation.navigate(direction, navOptions)
        }

        binding.kardioBox.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToHeartFragment()
            navigation.navigate(direction, navOptions)
        }

        binding.weightBox.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToWeightFragment()
            navigation.navigate(direction, navOptions)
        }

    }

}