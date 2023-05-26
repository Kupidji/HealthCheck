package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentMainBinding
import com.example.healthcheck.viewmodel.MainViewModel
import com.example.healthcheck.viewmodel.ViewPagerAdapter

class mainFragment : Fragment() {

    companion object {
        fun newInstance() = mainFragment()
    }

    private lateinit var binding : FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPagerAdapter = ViewPagerAdapter(this, viewModel.fragList)
        binding.viewPager.adapter = viewPagerAdapter

    }

}