package com.example.healthcheck.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentMainBinding
import com.example.healthcheck.model.global_notifications.service.NotificationService
import com.example.healthcheck.model.mainscreen.viewmodel.MainViewModel
import com.example.healthcheck.model.mainscreen.viewmodel.ViewPagerAdapter
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import kotlin.math.abs

class mainFragment : Fragment() {

    companion object {
        fun newInstance() = mainFragment()
    }

    private lateinit var binding : FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var nearestActMedicine : Medicines
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

        val viewPagerAdapter = ViewPagerAdapter(this, viewModel.fragList)
        binding.viewPager.adapter = viewPagerAdapter

        val navigation = findNavController()

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        var settings = this@mainFragment.context?.applicationContext?.getSharedPreferences(Constants.IS_FIRST_TIME, Context.MODE_PRIVATE)
        var settingsHealthyEat = this@mainFragment.context?.applicationContext?.getSharedPreferences(Constants.HEALTHY_EAT_VISIBILITY, Context.MODE_PRIVATE)

        if (settingsHealthyEat != null) {
            if (settingsHealthyEat.getBoolean(Constants.HEALTHY_EAT_VISIBILITY, true)) {
                binding.healthyEat.visibility = View.VISIBLE
            } else {
                binding.healthyEat.visibility = View.GONE
            }
        }

        if (settings != null) {
            if (settings.getBoolean(Constants.IS_FIRST_TIME, true)) {
                //переход в знакомство
                navigation.navigate(R.id.startFragment)
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

                val direction = mainFragmentDirections.actionMainFragmentToMedicinesEditFragment (
                    nearestActMedicine.title,
                    nearestActMedicine.timeOfNotify1,
                    nearestActMedicine.channelIDFirstTime,
                    nearestActMedicine.timeOfNotify2,
                    nearestActMedicine.channelIDSecondTime,
                    nearestActMedicine.timeOfNotify3,
                    nearestActMedicine.channelIDThirdTime,
                    nearestActMedicine.timeOfNotify4,
                    nearestActMedicine.channelIDFourthTime,
                    nearestActMedicine.dateStart,
                    nearestActMedicine.currentDayOfCourse,
                    nearestActMedicine.durationOfCourse,
                    nearestActMedicine.id,
                    nearestActMedicine.totalMissed,
                )
                navigation.navigate(direction, navOptions2)
            }
            else {
                Toast.makeText(this.requireContext(), "Добавьте действие", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getNearestAction()
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

    fun getNearestAction() {
        var nearestAction = Medicines(
            id = 0,
            title = "title",
            dateStart = 0,
            durationOfCourse = 0,
            currentDayOfCourse = 0,
            timeOfNotify1 = 0,
            channelIDFirstTime = 0,
            timeOfNotify2 = 0,
            channelIDSecondTime = 0,
            timeOfNotify3 = 0,
            channelIDThirdTime = 0,
            timeOfNotify4 = 0,
            channelIDFourthTime = 0,
            totalMissed = 0,
        )
        viewModel.getAllMedicines().observe(this@mainFragment.viewLifecycleOwner) { livedataList ->
            var currentTime = Calendar.getInstance().timeInMillis
            var tempMin = currentTime
            var tempNearestTime = 0L
            var list: List<Medicines> = livedataList
               for (medicine in list) {
                   var listOfNotifications = listOf(
                       medicine.timeOfNotify1,
                       medicine.timeOfNotify2,
                       medicine.timeOfNotify3,
                       medicine.timeOfNotify4,
                   )

                   for (action in listOfNotifications) {
                       var tempTime = abs(currentTime - action)
                       if (tempTime < tempMin) {
                           tempMin = tempTime
                           tempNearestTime = action
                           nearestAction = medicine
                           nearestAction.title = medicine.title
                       }
                   }
               }

            if (tempNearestTime != 0L) {
                binding.actions.text = nearestAction.title + " - " + SimpleDateFormat("HH:mm").format(tempNearestTime)+", через "+ forGmt(calculateTime(currentTime, tempNearestTime))
                nearestActMedicine = nearestAction
                isClickable = true
            }
            else {
                binding.actions.setText(R.string.no_nearest_act)
                isClickable = false
            }

        }
    }
    private fun getGMT() : String {
        val tz = TimeZone.getDefault()
        val gmt1 = TimeZone.getTimeZone(tz.id).getDisplayName(false, TimeZone.SHORT)
        return if (gmt1.length > 3){
            gmt1.slice(4..8)
        } else{
            "00:00"
        }
    }

    private fun forGmt(long: Long) : String {
        val GMT = getGMT()
        val listGMT = GMT.split(":")
        return SimpleDateFormat("HH:mm").format(long - listGMT[0].toInt() * 3600000)
    }

    private fun calculateTime(time1 : Long, time2 : Long) : Long {
        return if (time1 - time2 <= 0) {
            time2 - time1
        } else {
            time2 + 86400000 - time1
        }
    }
}