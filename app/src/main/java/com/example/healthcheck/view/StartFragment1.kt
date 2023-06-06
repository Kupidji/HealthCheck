package com.example.healthcheck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.healthcheck.databinding.FragmentStart1Binding

class StartFragment1 : Fragment() {
    companion object {
        fun newInstance() = StartFragment1()
    }

    private lateinit var binding : FragmentStart1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStart1Binding.inflate(inflater)
        return binding.root
    }
}