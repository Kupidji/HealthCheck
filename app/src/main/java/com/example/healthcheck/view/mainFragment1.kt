package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.model.mainscreen.viewmodel.MainFragment1ViewModel
import com.example.healthcheck.databinding.FragmentMain1Binding
import com.example.healthcheck.util.AppDispatchers
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.ProgressBarAnimation.animateProgressBar
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

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
        showAndUpdateStepsProgressBar()
        showAndUpdateWeightProgressBar()
        showAndUpdateSleep()

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

    private fun showAndUpdateStepsProgressBar() {
        var currentDate = Calendar.getInstance().timeInMillis

        binding.progressBarSteps.max = viewModel.settings.getInt(Constants.TARGET, 10000)
        lifecycleScope.launch {
            viewModel.daySteps.collect {
                if ((SimpleDateFormat("dd").format(it)) != (SimpleDateFormat("dd").format(currentDate))) {
                    binding.main1CountOfStepsDay.text = "0"
                    binding.progressBarSteps.progress = 0
                }
                else {
                    animateProgressBar(binding.progressBarSteps, viewModel.settings.getInt(Constants.STEPS_PER_DAY, 0))
                }
            }
        }

    }

    private fun showAndUpdateSleep() {
        var currentDate = Calendar.getInstance().timeInMillis

        lifecycleScope.launch {
            viewModel.daySleep.collect {
                if ((SimpleDateFormat("dd").format(it)) != (SimpleDateFormat("dd").format(currentDate))) {
                    binding.sleepHoursDay.setText("0:00")
                    val editorForSleep = viewModel.settingsForSleep.edit()
                    editorForSleep?.putLong(Constants.TIME_SLEEP, 0L)?.apply()
                }
                val sleepy = forGmt()
                binding.sleepHoursDay.text = sleepy.split(":")[0].toInt().toString() + ":" + sleepy.split(":")[1] + "ч"
            }
        }

    }

    private fun forGmt() : String {
        var GMT = getGMT()
        var listGMT = GMT.split(":")
        return SimpleDateFormat("HH:mm").format(viewModel.settingsForSleep.getLong(Constants.TIME_SLEEP, 0L) - listGMT[0].toInt() * 3600000)
    }

    private fun showAndUpdateWeightProgressBar() {
        lifecycleScope.launch {
            viewModel.dayWeight.collect {
                animateProgressBar(
                    binding.progressBarWeight,
                    it.toInt()
                )
            }
        }
        binding.progressBarWeight.max = 120
    }

    private fun showDigits() {
        binding.main1CountOfStepsDay.text = viewModel.settings.getInt(Constants.STEPS_PER_DAY, 0).toString()
        lifecycleScope.launch {
            viewModel.dayWeight.collect {
                binding.weightCountText.text = String.format(Locale.US,"%.1f", it)
            }
        }
        //binding.weightCountText.text = String.format(Locale.US,"%.1f",viewModel.settingsForWeight.getFloat(Constants.WEIGHT_FOR_DAY, 0F))

        val sleepy = forGmt()
        binding.sleepHoursDay.text = sleepy.split(":")[0].toInt().toString() + ":" + sleepy.split(":")[1] + "ч"
        binding.daylyCardio.text = viewModel.settingsForCardio.getString(Constants.PRESSURE, "0/0")
    }

    private fun getGMT() : String {
        val tz = TimeZone.getDefault()
        val gmt1 = TimeZone.getTimeZone(tz.id).getDisplayName(false, TimeZone.SHORT)
        if (gmt1.length > 3){
            return gmt1.slice(4..8)
        }
        else{
            return "00:00"
        }
    }

}