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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.viewmodel.MainFragment2ViewModel
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentMain1Binding
import com.example.healthcheck.databinding.FragmentMain2Binding
import com.example.healthcheck.util.Constants
import com.example.healthcheck.viewmodel.MainFragment1ViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

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

        //val tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())

        viewModel.totalStepsForWeek.observe(this@mainFragment2.viewLifecycleOwner) {
            binding.main2CountOfStepsWeek.setText("${it}")
        }
        viewModel.totalStepsForWeek.observe(this@mainFragment2.viewLifecycleOwner) {
            if (it != null) {
                binding.progressBarSteps.progress = it
            }
        }

        binding.progressBarSteps.max = viewModel.settings.getInt(Constants.TARGET, 10000)

        viewModel.averageSleep.observe(this@mainFragment2.viewLifecycleOwner) {
            binding.sleepHours.text = it
        }


        viewModel.totalWeightForWeight.observe(this@mainFragment2.viewLifecycleOwner) {
            if (it != null) {
                binding.progressBarWeight.progress = (it).toInt()
            }
            binding.weekWeightText.text = String.format("%.1f",it)
        }

        binding.progressBarWeight.max = 120

        binding.stepsBox.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToStepsFragment()

            //анимация
            binding.stepsLayout.animate()
                .setDuration(25)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .withEndAction {
                    //навигация
                    navigation.navigate(direction, navOptions)
                }
        }

        binding.sleepBox.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToSleepFragment()

            //анимация
            binding.sleepLayout.animate()
                .setDuration(25)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .withEndAction {
                    //навигация
                    navigation.navigate(direction, navOptions)
                }
        }

        binding.kardioBox.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToHeartFragment()

            //анимация
            binding.kardioLayout.animate()
                .setDuration(25)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .withEndAction {
                    //навигация
                    navigation.navigate(direction, navOptions)
                }
        }

        binding.weightBox.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToWeightFragment()

            //анимация
            binding.weightLayout.animate()
                .setDuration(25)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .withEndAction {
                    //навигация
                    navigation.navigate(direction, navOptions)
                }

        }

    }

}