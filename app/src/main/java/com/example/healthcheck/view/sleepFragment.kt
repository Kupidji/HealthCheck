package com.example.healthcheck.view

import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentSleepBinding
import com.example.healthcheck.model.sleep.entities.Sleep
import com.example.healthcheck.util.Constants
import com.example.healthcheck.viewmodel.SleepViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class sleepFragment : Fragment() {

    companion object {
        fun newInstance() = sleepFragment()
    }

    private lateinit var viewModel: SleepViewModel
    private lateinit var binding : FragmentSleepBinding
    private var goesToBedTime = 0L
    private var wakeUpTime = 0L

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
        val tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())

        val navigation = findNavController()

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            binding.timeForWeek.setText("${viewModel.getSleepFromDataForWeek(tripletsPool) + "ч"}")
        }

        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            binding.timeForMonth.setText("${viewModel.getSleepFromDataForMonth(tripletsPool) + "ч"}")
        }

        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            binding.averageSleepWeek.setText("${viewModel.getSleepFromDataForWeekAverage(tripletsPool) + "ч"}")
        }

        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            binding.averageSleepMonth.setText("${viewModel.getSleepFromDataForMonthAverage(tripletsPool) + "ч"}")
        }

        binding.wentBack.setOnClickListener {
            if (binding.getGoesToBedTime.text.isNotEmpty() && binding.getWakeUpTime.text.isNotEmpty()) {
                var currentTime = Calendar.getInstance().timeInMillis
                saveSleepSettings(calculateTimeBetweenStartEndSleep(goesToBedTime, wakeUpTime))
                var sleep = Sleep(
                    calculateTimeBetweenStartEndSleep(goesToBedTime, wakeUpTime),
                    currentTime,
                )
                viewModel.insertSleep(sleep)
            }

            val direction = sleepFragmentDirections.actionSleepFragmentToMainFragment()

            //анимация
            binding.wentBack.animate()
                .setDuration(25)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .withEndAction {
                    //навигация
                    navigation.navigate(direction, navOptions)
                }
        }

        binding.profile.setOnClickListener {
            val direction = sleepFragmentDirections.actionSleepFragmentToProfileFragment()

            //анимация
            binding.profile.animate()
                .setDuration(25)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .withEndAction {
                    //навигация
                    navigation.navigate(direction, navOptions)
                }
        }

        binding.sleep1.setOnClickListener {
            setAlarm(binding.getGoesToBedTime) { callback ->
                goesToBedTime = callback
            }
        }

        binding.sleep2.setOnClickListener {
            setAlarm(binding.getWakeUpTime) { callback ->
                wakeUpTime = callback
            }
        }

    }

    private fun saveSleepSettings(sleep : Long) {

        val editor = viewModel.settings.edit()
        editor?.putLong(
            Constants.TIME_SLEEP,
            sleep
        )?.apply()

    }

    private fun calculateTimeBetweenStartEndSleep(time1 : Long, time2 : Long) : Long {
        if (time1 - time2 <= 0) {
            return time1 - time2
        }
        else {
            return 86400000 - abs(time2 - time1)
        }
    }

    private fun setAlarm(textView: TextView, callback: (Long) -> Unit) {
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
                    textView.text = SimpleDateFormat("HH:mm").format(this.time)
                },
                this.get(Calendar.HOUR_OF_DAY),
                this.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

}