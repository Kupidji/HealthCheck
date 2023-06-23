package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.viewmodel.MainFragment1ViewModel
import com.example.healthcheck.databinding.FragmentMain1Binding
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.ProgressBarAnimation.animateProgressBar
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

        showDigits()

        binding.sleepHoursDay.text = SimpleDateFormat("HH:mm").format(viewModel.settingsForSleep.getLong(Constants.TIME_SLEEP, 0L) - 18000000) + "ч"

        binding.stepsBox.setOnClickListener {
            //навигация и анимации
            val direction = mainFragmentDirections.actionMainFragmentToStepsFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.stepsLayout, navigation, direction, navOptions, navigate)
        }

        binding.sleepBox.setOnClickListener {
            //навигация и анимации
            val direction = mainFragmentDirections.actionMainFragmentToSleepFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.sleepLayout, navigation, direction, navOptions, navigate)
        }

        binding.kardioBox.setOnClickListener {
            //навигация и анимации
            val direction = mainFragmentDirections.actionMainFragmentToHeartFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.kardioLayout, navigation, direction, navOptions, navigate)
        }

        binding.weightBox.setOnClickListener {
            //навигация и анимации
            val direction = mainFragmentDirections.actionMainFragmentToWeightFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.weightLayout, navigation, direction, navOptions, navigate)
        }

    }

    override fun onResume() {
        super.onResume()
        showAndStepsUpdateProgressBar()
        showAndUpdateWeight()
    }

    override fun onPause() {
        super.onPause()
        binding.progressBarSteps.progress = 0
        binding.progressBarWeight.progress = 0
    }

    private fun showAndStepsUpdateProgressBar() {
        var currentDate = Calendar.getInstance().timeInMillis

        binding.progressBarSteps.max = viewModel.settings.getInt(Constants.TARGET, 10000)
        viewModel.daySteps.observe(this@mainFragment1.viewLifecycleOwner) {
            if ((SimpleDateFormat("dd").format(it)) != (SimpleDateFormat("dd").format(currentDate))) {
                binding.main1CountOfStepsDay.text = "0"
                binding.progressBarSteps.progress = 0
            }
            else {
                animateProgressBar(binding.progressBarSteps, viewModel.settings.getInt(Constants.STEPS_PER_DAY, 0))
            }
        }
    }

    private fun showAndUpdateWeight() {
        var currentDate = Calendar.getInstance().timeInMillis
        viewModel.dayWeight.observe(this@mainFragment1.viewLifecycleOwner) {
            if ((SimpleDateFormat("dd").format(it)) != (SimpleDateFormat("dd").format(currentDate))) {
                binding.weightCountText.text = 0F.toString()
                binding.progressBarWeight.progress = 0
            }
            else {
                animateProgressBar(binding.progressBarWeight, viewModel.settingsForWeight.getFloat(Constants.WEIGHT_FOR_DAY, 0F).toInt())
            }
        }
        binding.progressBarWeight.max = 120
    }

    private fun showDigits() {
        binding.main1CountOfStepsDay.text = viewModel.settings.getInt(Constants.STEPS_PER_DAY, 0).toString()
        binding.weightCountText.text = String.format(Locale.US,"%.1f",viewModel.settingsForWeight.getFloat(Constants.WEIGHT_FOR_DAY, 0F))
    }

}