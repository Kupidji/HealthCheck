package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.viewmodel.MainFragment3ViewModel
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentMain2Binding
import com.example.healthcheck.databinding.FragmentMain3Binding
import com.example.healthcheck.viewmodel.MainFragment2ViewModel

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