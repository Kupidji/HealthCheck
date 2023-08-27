package com.example.healthcheck.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.domain.AppDispatchers
import com.example.healthcheck.databinding.FragmentMain2Binding
import com.example.healthcheck.util.animations.DigitsAnimation
import com.example.healthcheck.util.animations.DigitsAnimation.digitsFloatAnimation
import com.example.healthcheck.util.animations.DigitsAnimation.digitsIntAnimation
import com.example.healthcheck.util.animations.ProgressBarAnimation.animateProgressBar
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.main.MainFragment2ViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class mainFragment2 : Fragment() {

    companion object {
        fun newInstance() = mainFragment2()
    }

    private lateinit var viewModel: MainFragment2ViewModel
    private lateinit var binding : FragmentMain2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MainFragment2ViewModel::class.java)
        binding = FragmentMain2Binding.inflate(inflater)
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

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.totalStepsForWeek.collect { steps ->
                digitsIntAnimation(binding.main2CountOfStepsWeek, steps)
                showAndUpdateProgressBar(progressBar = binding.progressBarSteps, progress = steps)
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.stepsTarget.collect { target ->
                binding.progressBarSteps.max = target * 7
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.averageSleepWeek.collect { time ->
                binding.sleepHoursWeek.text = "${time}ч"
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.totalWeightForWeek.collect { weight ->
                digitsFloatAnimation(binding.weekWeightText, weight)
                showAndUpdateProgressBar(progressBar = binding.progressBarWeight, progress = weight.toInt())
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.weightTarget.collect { target ->
                binding.progressBarWeight.max = target.toInt()
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.weekHeart.collect { string ->
                var ourString = string
                val upPressure = ourString.substringBefore("/")
                ourString = ourString.drop(upPressure.length + 1)
                val downPressure = ourString.substringBefore("/")
                ourString = ourString.drop(downPressure.length + 1)
                val pulse = ourString
                if (upPressure.isNotEmpty()) {
                    binding.upPressureText.text = upPressure
                }
                if (downPressure.isNotEmpty()) {
                    binding.downPressureText.text = downPressure
                }
                if (pulse.isNotEmpty()) {
                    binding.pulseText.text = pulse
                }
            }
        }

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

    private fun showAndUpdateProgressBar(progressBar: ProgressBar, progress : Int) {
        animateProgressBar(progressBar, progress)
    }

}