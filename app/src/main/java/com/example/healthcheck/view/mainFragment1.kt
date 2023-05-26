package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.viewmodel.MainFragment1ViewModel
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentMain1Binding

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

        binding.stepsBox.setOnClickListener {
            navigation.navigate(R.id.stepsFragment)
        }

        binding.sleepBox.setOnClickListener {
            navigation.navigate(R.id.sleepFragment)
        }

        binding.heartBox.setOnClickListener {
            navigation.navigate(R.id.heartFragment)
        }

        binding.weightBox.setOnClickListener {
            navigation.navigate(R.id.weightFragment)
        }

    }

}