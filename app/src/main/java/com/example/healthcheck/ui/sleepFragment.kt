package com.example.healthcheck.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.domain.AppDispatchers
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentSleepBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.SleepViewModel
import kotlinx.coroutines.launch
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.Calendar

class sleepFragment : Fragment() {

    companion object {
        fun newInstance() = sleepFragment()
    }

    private lateinit var viewModel: SleepViewModel
    private lateinit var binding : FragmentSleepBinding
    private var _goesToBedTime = 0L
    private var _wakeUpTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SleepViewModel::class.java)
        binding = FragmentSleepBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        var _id = 0
        var _date = 0L

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.date.collect { date ->
                if (date != null) {
                    _date = date
                }
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.id.collect { id ->
                if (id != null) {
                    _id = id
                }
            }
        }


        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.sleepForDay.collect { time ->
                binding.getGoesToBedTime.text = time
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.sleepForWeek.collect { time ->
                binding.timeForWeek.text = "$time ч"
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.sleepForMonth.collect { time ->
                binding.timeForMonth.text = "$time ч"
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.averageSleepForWeek.collect { time ->
                binding.averageSleepWeek.text = "${time + "ч"}"
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.averageSleepForMonth.collect { time ->
                binding.averageSleepMonth.text = "${time + "ч"}"
            }
        }

        binding.sleep1.setOnClickListener {
            setAlarm { callback ->
                _goesToBedTime = callback
                viewModel.setGoToSleepTime(time = callback)
                setAlarm { callback2 ->
                    _wakeUpTime = callback2
                    viewModel.setWakeUpTime(time = callback2)
                    saveSleep(_id, _date)
                }
            }
        }

        binding.wentBack.setOnClickListener {
            //навигация и анимации
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.mainFragment, true)
                .build()
            val direction = sleepFragmentDirections.actionSleepFragmentToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

    }

    private fun saveSleep(id: Int, date : Long) {
        if (_goesToBedTime != 0L && _wakeUpTime != 0L) {
            saveOrUpdateSleepBd(id, date)
        }
    }

    private fun setAlarm(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            TimePickerDialog(
                this@sleepFragment.context,
                0,
                { _, hour, minute ->
                    this.set(Calendar.HOUR_OF_DAY, hour)
                    this.set(Calendar.MINUTE, minute)
                    callback(this.timeInMillis)
                },
                this.get(Calendar.HOUR_OF_DAY),
                this.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun saveOrUpdateSleepBd(id: Int, date: Long) {
        val currentDate = Calendar.getInstance().timeInMillis
        Log.d("date", "current date: $currentDate day: $date id : $id ")
        //Если новая дата не совпадает со старой -> insert
        if (SimpleDateFormat("dd.MM").format(currentDate) != SimpleDateFormat("dd.MM").format(date)) {
            viewModel.insertSleep(goToSleepTime = _goesToBedTime, wakeUpTime = _wakeUpTime)
        }
        else { //Если новая дата совпадает со старой -> update
            viewModel.updateSleep(id = id, goToSleepTime = _goesToBedTime, wakeUpTime = _wakeUpTime)
        }
    }

}