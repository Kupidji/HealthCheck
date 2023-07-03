package com.example.healthcheck.view

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentSleepBinding
import com.example.healthcheck.model.sleep.entities.Sleep
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodel.SleepViewModel
import java.text.SimpleDateFormat
import java.util.Calendar


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

        val navigation = findNavController()

        viewModel.sleepForWeek.observe(this@sleepFragment.viewLifecycleOwner) {
            binding.timeForWeek.setText("${it + "ч"}")
        }

        viewModel.sleepForMonth.observe(this@sleepFragment.viewLifecycleOwner) {
            binding.timeForMonth.setText("${it + "ч"}")
        }

        viewModel.averageSleepForWeek.observe(this@sleepFragment.viewLifecycleOwner) {
            binding.averageSleepWeek.setText("${it + "ч"}")
        }

        viewModel.averageSleepForMonth.observe(this@sleepFragment.viewLifecycleOwner) {
            binding.averageSleepMonth.setText("${it + "ч"}")
        }

        binding.wentBack.setOnClickListener {
            if (binding.getGoesToBedTime.text.isNotEmpty() && binding.getWakeUpTime.text.isNotEmpty()) {
                saveSleepSettings(calculateTimeBetweenStartEndSleep(goesToBedTime, wakeUpTime))
            }
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

        binding.sleep1.setOnClickListener {
            setAlarm(binding.getGoesToBedTime) { callback ->
                goesToBedTime = callback
                saveSleep()
            }

        }

        binding.sleep2.setOnClickListener {
            setAlarm(binding.getWakeUpTime) { callback ->
                wakeUpTime = callback
                saveSleep()
            }

        }

    }

    private fun saveSleep() {
        if (goesToBedTime != 0L && wakeUpTime != 0L) {
            saveOrUpdateSleepBd()
            viewModel.setCurrentAverageSleepWeek()
            viewModel.setCurrentSleepWeek()
            viewModel.setCurrentSleepMonth()
            viewModel.setCurrentAverageSleepMonth()
            viewModel.setCurrentId()
            viewModel.setCurrentDate()
            saveSleepSettings(calculateTimeBetweenStartEndSleep(goesToBedTime, wakeUpTime))
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
            return time2 - time1
        }
        else {
            val time2Cur = time2 + 86400000
            return time2Cur - time1
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

    private fun saveOrUpdateSleepBd() {

        var date = 0L
        var id = 0
        val currentDate = Calendar.getInstance().timeInMillis

        viewModel.lastDate.observe(this@sleepFragment.viewLifecycleOwner) {
            if (it != null) {
                date = it
            }
        }
        viewModel.lastId.observe(this@sleepFragment.viewLifecycleOwner) {
            if (it != null) {
                id = it
            }
        }

        //Если новая дата не совпадает со старой -> insert
        if (SimpleDateFormat("dd.MM").format(currentDate) != SimpleDateFormat("dd.MM").format(date)) {

            val ourSleep = forSleepBd(currentDate, 0)
            viewModel.insertSleep(ourSleep)

        } else { //Если новая дата совпадает со старой -> update

            val ourSleep = forSleepBd(currentDate, id)
            viewModel.updateSleep(ourSleep)

        }

    }

    private fun forSleepBd(currentDate: Long, id: Int): Sleep {
        return Sleep(
            id = id,
            timeOfSleep = calculateTimeBetweenStartEndSleep(goesToBedTime, wakeUpTime),
            date = currentDate,
        )
    }

}