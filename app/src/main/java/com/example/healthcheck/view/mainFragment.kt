package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            navigation.navigate(R.id.settingsFragment)
        }

        binding.profile.setOnClickListener {
            navigation.navigate(R.id.profileFragment)
        }

        binding.medicineiesBox.setOnClickListener {
            navigation.navigate(R.id.medicinesFragment)
        }

        binding.healtyEatBox.setOnClickListener {
            navigation.navigate(R.id.nutritionFragment)
        }

    }

    fun movePagesViewPager(position : Int) {
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
                binding.todayTextBackground.visibility = View.VISIBLE
                binding.todayText.setTextColor(resources.getColor(R.color.textcolor_white))
                binding.weekTextBackground.visibility = View.GONE
                binding.weekText.setTextColor(resources.getColor(R.color.textcolor_black))
                binding.monthTextBackground.visibility = View.GONE
                binding.monthText.setTextColor(resources.getColor(R.color.textcolor_black))
            }
            1 -> {

                binding.weekTextBackground.visibility = View.VISIBLE
                binding.weekText.setTextColor(resources.getColor(R.color.textcolor_white))
                binding.todayTextBackground.visibility = View.GONE
                binding.todayText.setTextColor(resources.getColor(R.color.textcolor_black))
                binding.monthTextBackground.visibility = View.GONE
                binding.monthText.setTextColor(resources.getColor(R.color.textcolor_black))
            }
            2 -> {

                binding.monthTextBackground.visibility = View.VISIBLE
                binding.monthText.setTextColor(resources.getColor(R.color.textcolor_white))
                binding.todayTextBackground.visibility = View.GONE
                binding.todayText.setTextColor(resources.getColor(R.color.textcolor_black))
                binding.weekTextBackground.visibility = View.GONE
                binding.weekText.setTextColor(resources.getColor(R.color.textcolor_black))
            }
        }
    }

}