package com.example.healthcheck.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.domain.AppDispatchers
import com.example.healthcheck.databinding.FragmentMain1Binding
import com.example.healthcheck.util.animations.ProgressBarAnimation.animateProgressBar
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.main.MainFragment1ViewModel
import kotlinx.coroutines.launch
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

        lifecycleScope.launch {
            viewModel.stepsTarget.collect { target ->
                binding.progressBarSteps.max = target
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.daySteps.collect { steps ->
                if (steps != 0) {
                    binding.main1CountOfStepsDay.text = steps.toString()
                }
                else {
                    binding.main1CountOfStepsDay.text = "?"
                }
                showAndUpdateStepsProgressBar(progress = steps)
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.dayWeight.collect { weight ->
                if (weight != 0F) {
                    binding.weightCountText.text = String.format(Locale.US,"%.1f", weight)
                }
                else {
                    binding.weightCountText.text = "?"
                }
                showAndUpdateWeightProgressBar(progress = weight)
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.weightTarget.collect { target ->
                binding.progressBarWeight.max = target.toInt()
            }
        }

        lifecycleScope.launch {
            viewModel.daySleep.collect { time ->
                binding.sleepHoursDay.text = "${time}ч"
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

    private fun showAndUpdateStepsProgressBar(progress : Int) {
        animateProgressBar(
            binding.progressBarSteps,
            progress
        )
    }

    private fun showAndUpdateWeightProgressBar(progress : Float) {
        animateProgressBar(
            binding.progressBarWeight,
            progress.toInt()
        )
    }

}