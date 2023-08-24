package com.example.healthcheck.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.domain.AppDispatchers
import com.example.domain.models.Medicines
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentMainBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.MainViewModel
import com.example.healthcheck.viewmodels.ViewPagerAdapter
import kotlinx.coroutines.launch

class mainFragment : Fragment() {

    companion object {
        fun newInstance() = mainFragment()
    }

    private lateinit var binding : FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var _nearestActMedicine : Medicines
    private var isClickable = false

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

        val navigation = findNavController()

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        val viewPagerAdapter = ViewPagerAdapter(this, viewModel.fragList)
        binding.viewPager.adapter = viewPagerAdapter

//        lifecycleScope.launch(AppDispatchers.main) {
//            viewModel.isFirstLaunch.collect { status ->
//                if (status) {
//                    navigation.navigate(R.id.startFragment)
//                }
//            }
//        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.nearestAction.collect { action ->
                _nearestActMedicine = action
                isClickable = (action.title != "")
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.nearestTime.collect { action ->
                binding.actions.text = action
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.visibilityOfNutrition.collect { visibility ->
                if (visibility) {
                    binding.healthyEat.visibility = View.VISIBLE
                }
                else {
                    binding.healthyEat.visibility = View.GONE
                }
            }
        }

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
            //навигация и аниманции
            val direction = mainFragmentDirections.actionMainFragmentToSettingsFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.settings, navigation, direction, navOptions, navigate)
        }

        binding.profile.setOnClickListener {
            //навигация и анимации
            val direction = mainFragmentDirections.actionMainFragmentToProfileFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.profile, navigation, direction, navOptions, navigate)
        }

        binding.medicineiesBox.setOnClickListener {
            val direction = mainFragmentDirections.actionMainFragmentToMedicinesFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.medicineiesBox, navigation, direction, navOptions, navigate)
        }

        binding.healthyEat.setOnClickListener {
            //навигация и анимации
            val direction = mainFragmentDirections.actionMainFragmentToNutritionFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.healthyEat, navigation, direction, navOptions, navigate)
        }

        binding.nearestActBox.setOnClickListener {
            if (isClickable) {
                var navOptions2 = NavOptions.Builder()
                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                    .setPopUpTo(R.id.medicinesEditFragment, true)
                    .build()

                val direction = mainFragmentDirections.actionMainFragmentToMedicinesEditFragment(
                    _nearestActMedicine.title,
                    _nearestActMedicine.timeOfNotify1,
                    _nearestActMedicine.channelIDFirstTime,
                    _nearestActMedicine.timeOfNotify2,
                    _nearestActMedicine.channelIDSecondTime,
                    _nearestActMedicine.timeOfNotify3,
                    _nearestActMedicine.channelIDThirdTime,
                    _nearestActMedicine.timeOfNotify4,
                    _nearestActMedicine.channelIDFourthTime,
                    _nearestActMedicine.dateStart,
                    _nearestActMedicine.currentDayOfCourse,
                    _nearestActMedicine.durationOfCourse,
                    _nearestActMedicine.id,
                    _nearestActMedicine.totalMissed,
                )
                navigation.navigate(direction, navOptions2)
            }
            else {
                Toast.makeText(this.requireContext(), "Добавьте действие", Toast.LENGTH_SHORT).show()
            }
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