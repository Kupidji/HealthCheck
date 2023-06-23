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
import com.example.healthcheck.viewmodel.MainFragment3ViewModel
import com.example.healthcheck.databinding.FragmentMain3Binding
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.ProgressBarAnimation.animateProgressBar
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import java.util.Locale

class mainFragment3 : Fragment() {

    companion object {
        fun newInstance() = mainFragment3()
    }

    private lateinit var viewModel: MainFragment3ViewModel
    private lateinit var binding : FragmentMain3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MainFragment3ViewModel::class.java)
        binding = FragmentMain3Binding.inflate(inflater)
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

        viewModel.averageSleepMonth.observe(this@mainFragment3.viewLifecycleOwner) {
            binding.sleepHoursMonth.text = it
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
            val direction = mainFragmentDirections.actionMainFragmentToWeightFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.weightLayout, navigation, direction, navOptions, navigate)

            //анимация
            binding.weightLayout.animate()
                .setDuration(75)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .withEndAction {
                    //навигация
                    navigation.navigate(direction, navOptions)
                }

        }

    }

    override fun onResume() {
        super.onResume()
        showAndUpdateStepsProgressBar()
        showAndUpdateWeightProgressBar()
    }

    override fun onPause() {
        super.onPause()
        binding.progressBarSteps.progress = 0
        binding.progressBarWeight.progress = 0
    }

    private fun showAndUpdateStepsProgressBar() {
        binding.progressBarSteps.max = viewModel.settings.getInt(Constants.TARGET, 10000) * 30
        viewModel.totalStepsForMonth.observe(this@mainFragment3.viewLifecycleOwner) {
            if (it != null) {
                animateProgressBar(binding.progressBarSteps, it)
            }
        }
    }

    private fun showAndUpdateWeightProgressBar() {
        binding.progressBarWeight.max = 120
        viewModel.totalWeightForMonth.observe(this@mainFragment3.viewLifecycleOwner) {
            if (it != null) {
                animateProgressBar(binding.progressBarWeight, it.toInt())
            }
        }
    }

    private fun showDigits() {
        viewModel.totalStepsForMonth.observe(this@mainFragment3.viewLifecycleOwner) {
            binding.main3CountOfStepsMonth.setText("${it}")

        }
        viewModel.totalWeightForMonth.observe(this@mainFragment3.viewLifecycleOwner) {
            binding.textWeightMonth.text = String.format(Locale.US, "%.1f", it)
        }
    }

}