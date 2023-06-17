package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
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

        val navigation = findNavController()

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()


        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                scrollViewPager(position)
            }
        })

        binding.todayText.setOnClickListener {
            movePagesViewPager(1)
        }

        binding.weekText.setOnClickListener {
            movePagesViewPager(2)
        }

        binding.monthText.setOnClickListener {
            movePagesViewPager(3)
        }

        binding.settings.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToSettingsFragment()
            navigation.navigate(direction, navOptions)
        }

        binding.profile.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToProfileFragment()
            navigation.navigate(direction, navOptions)
        }

        binding.medicineiesBox.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToMedicinesFragment()
            navigation.navigate(direction, navOptions)
        }

        binding.healtyEatBox.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToNutritionFragment()
            navigation.navigate(direction, navOptions)
        }

    }

    private fun movePagesViewPager(position : Int) {
        when(position) {
            1 -> {
                binding.viewPager.currentItem = 0
                scrollViewPager(0)
            }
            2 -> {
                binding.viewPager.currentItem = 1
                scrollViewPager(1)
            }
            3 -> {
                binding.viewPager.currentItem = 2
                scrollViewPager(2)
            }
        }
    }

    private fun scrollViewPager(position : Int) {
        when (position) {
            0 -> {
                setChoosenBtn(binding.todayText, binding.todayTextBackground)
                setUnchoosenBtn(binding.weekText, binding.weekTextBackground)
                setUnchoosenBtn(binding.monthText, binding.monthTextBackground)
            }
            1 -> {
                setUnchoosenBtn(binding.todayText, binding.todayTextBackground)
                setChoosenBtn(binding.weekText, binding.weekTextBackground)
                setUnchoosenBtn(binding.monthText, binding.monthTextBackground)
            }
            2 -> {
                setUnchoosenBtn(binding.todayText, binding.todayTextBackground)
                setUnchoosenBtn(binding.weekText, binding.weekTextBackground)
                setChoosenBtn(binding.monthText, binding.monthTextBackground)
            }
        }
    }

    private fun setChoosenBtn(view : TextView, background : View) {
        background.visibility = View.VISIBLE
        view.setTextColor(ContextCompat.getColor(requireContext(), R.color.textcolor_choosen))
    }

    private fun setUnchoosenBtn(view : TextView, background : View) {
        background.visibility = View.GONE
        view.setTextColor(ContextCompat.getColor(requireContext(), R.color.textcolor_unchoosen))
    }

}