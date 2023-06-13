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

        viewModel.totalStepsForMonth.observe(this@mainFragment3.viewLifecycleOwner) {
            binding.main3CountOfStepsMonth.setText("${it}")
        }
        viewModel.totalStepsForMonth.observe(this@mainFragment3.viewLifecycleOwner) {
            if (it != null) {
                binding.progressBarSteps.progress = it
            }
        }

        binding.progressBarSteps.max = viewModel.settings.getInt(Constants.TARGET, 10000)

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