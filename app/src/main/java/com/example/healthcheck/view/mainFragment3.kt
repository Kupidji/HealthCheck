package com.example.healthcheck.view

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.SharedMemory
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.viewmodel.MainFragment3ViewModel
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentMain2Binding
import com.example.healthcheck.databinding.FragmentMain3Binding
import com.example.healthcheck.util.Constants
import com.example.healthcheck.viewmodel.MainFragment2ViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

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


        viewModel.totalStepsForMonth.observe(this@mainFragment3.viewLifecycleOwner) {
            binding.main3CountOfStepsMonth.setText("${it}")
        }
        viewModel.totalStepsForMonth.observe(this@mainFragment3.viewLifecycleOwner) {
            if (it != null) {
                binding.progressBarSteps.progress = it
            }
        }

        viewModel.averageSleepMonth.observe(this@mainFragment3.viewLifecycleOwner) {
            binding.sleepHoursMonth.text = it
        }

        binding.progressBarSteps.max = viewModel.settings.getInt(Constants.TARGET, 10000)

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