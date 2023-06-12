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