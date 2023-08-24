package com.example.healthcheck.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.healthcheck.databinding.FragmentStart2Binding


class StartFragment2 : Fragment() {
    companion object {
        fun newInstance() = StartFragment2()
    }

    private lateinit var binding : FragmentStart2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStart2Binding.inflate(inflater)
        return binding.root
    }
}