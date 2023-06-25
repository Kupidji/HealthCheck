package com.example.healthcheck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.databinding.FragmentMain2Binding
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.ProgressBarAnimation.animateProgressBar
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.model.mainscreen.viewmodel.MainFragment2ViewModel
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

        //Заполняет цифры внутри progressBar
        showDigits()

        viewModel.averageSleep.observe(this@mainFragment2.viewLifecycleOwner) {
            binding.sleepHours.text = it + "ч"
        }

        binding.daylyCardio.text = viewModel.settingsForCardio.getString(Constants.PRESSURE, "0/0")

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
        showAndUpdateStepsProgressBar()
        showAndUpdateWeightProgressBar()
    }

    override fun onPause() {
        super.onPause()
        binding.progressBarSteps.progress = 0
        binding.progressBarWeight.progress = 0
    }

    private fun showAndUpdateStepsProgressBar() {
        binding.progressBarSteps.max = viewModel.settings.getInt(Constants.TARGET, 10000) * 7
        viewModel.totalStepsForWeek.observe(this@mainFragment2.viewLifecycleOwner) {
            if (it != null) {
                animateProgressBar(binding.progressBarSteps, it)
            }
        }
    }

    private fun showAndUpdateWeightProgressBar() {
        binding.progressBarWeight.max = 120
        viewModel.totalWeightForWeight.observe(this@mainFragment2.viewLifecycleOwner) {
            if (it != null) {
                animateProgressBar(binding.progressBarWeight, it.toInt())
            }
        }
    }

    private fun showDigits() {
        viewModel.totalStepsForWeek.observe(this@mainFragment2.viewLifecycleOwner) {
            binding.main2CountOfStepsWeek.setText("${it}")
        }
        viewModel.totalWeightForWeight.observe(this@mainFragment2.viewLifecycleOwner) {
            binding.weekWeightText.text = String.format(Locale.US, "%.1f", it)
        }
    }

}