package com.example.healthcheck.ui.sleep

import android.app.AlertDialog
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.AppDispatchers
import com.example.domain.models.Sleep
import com.example.healthcheck.R
import com.example.healthcheck.databinding.DialogEditSleepHistoryBinding
import com.example.healthcheck.databinding.FragmentSleepHistoryBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.sleep.SleepActionListener
import com.example.healthcheck.viewmodels.sleep.SleepHistoryRecyclerViewAdapter
import com.example.healthcheck.viewmodels.sleep.SleepHistoryViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SleepHistory : Fragment() {

    companion object {
        fun newInstance() = SleepHistory()
    }

    private lateinit var viewModel : SleepHistoryViewModel
    private lateinit var binding : FragmentSleepHistoryBinding
    private lateinit var adapter : SleepHistoryRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSleepHistoryBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(SleepHistoryViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        val layoutManager = LinearLayoutManager(this.requireContext())
        adapter = SleepHistoryRecyclerViewAdapter(object : SleepActionListener {
            override fun onBoxClickAction(sleep: Sleep) {
                showAlertDialog(sleep = sleep)
            }

        })

        lifecycleScope.launch(AppDispatchers.main) {
            binding.sleepHistoryRecyclerView.adapter = adapter
            binding.sleepHistoryRecyclerView.layoutManager = layoutManager
            viewModel.sleepListHistory.collect { list ->
                adapter.list = list
                if (list.isEmpty()) {
                    binding.nothingThere.visibility = View.VISIBLE
                }
                else {
                    binding.nothingThere.visibility = View.GONE
                }
            }
        }

        binding.wentBack.setOnClickListener {
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.sleepFragment, true)
                .build()
            //навигация и анимации
            val direction = SleepHistoryDirections.actionSleepHistoryToSleepFragment()
            val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

    }

    private fun showAlertDialog(sleep: Sleep) {
        val inflater = this.layoutInflater
        val dialogBinding = DialogEditSleepHistoryBinding.inflate(inflater)
        val dialogView = dialogBinding.root
        val layout = dialogBinding.textInputLayout
        dialogBinding.oldValueText.text = "${this.context?.getString(R.string.oldValue)} ${showTime(totalTime = sleep.timeOfSleep)}"
        var goToBedTime = 0L
        var wakeUpTime = 0L

        val dialog = MaterialAlertDialogBuilder(this.requireContext(), com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setView(dialogView)
            .setTitle(this.context?.getString(R.string.editHistory))
            .setPositiveButton(this.context?.getString(R.string.save), null)
            .setNegativeButton("Отмена", null)
            .show()

        dialogBinding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                dialogBinding.editText.isCursorVisible = false
                setAlarm { callback ->
                    goToBedTime = callback
                    setAlarm { callback2 ->
                        wakeUpTime = callback2
                        dialogBinding.editText.setText(showDiffBetweenTimes(time1 = callback, time2 = callback2))
                    }
                }
            }
        }

        dialogBinding.editText.setOnClickListener {
            setAlarm { callback ->
                goToBedTime = callback
                dialogBinding.editText.setText(SimpleDateFormat("HH:mm", Locale.getDefault()).format(callback))
            }
        }

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            val enteredText = dialogBinding.editText.text.toString()
            var status = true

            if (enteredText.isNotEmpty()) {
                if (goToBedTime != 0L) {
                    viewModel.updateSleep(sleep = sleep, calculateTimeBetweenStartEndSleep(time1 = goToBedTime, time2 = wakeUpTime))
                    Toast.makeText(
                        this.requireContext(),
                        this.context?.getString(R.string.historyUpdated),
                        Toast.LENGTH_SHORT
                    ).show()
                    status = false
                }
            }
            else {
                layout.error = this.context?.getString(R.string.emptyString)
            }

            if (!status) {
                dialog.dismiss()
            }
        }

        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun setAlarm(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            TimePickerDialog(
                this@SleepHistory.context,
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

    private fun showTime(totalTime : Long) : String {
        val hours = Math.abs((totalTime) / 1000 / 60 / 60).toString()
        var minutes = Math.abs((totalTime) / 1000 / 60 % 60).toString()
        if (minutes.length % 2 != 0)
            minutes = "0$minutes"
        return "$hours:$minutes"
    }

    private fun calculateTimeBetweenStartEndSleep(time1 : Long, time2 : Long) : Long {
        return if (time1 - time2 <= 0) {
            time2 - time1
        } else {
            val time2Cur = time2 + 86400000
            time2Cur - time1
        }
    }

    private fun showDiffBetweenTimes(time1 : Long, time2 : Long) : String {
        val totalTime = calculateTimeBetweenStartEndSleep(time1 = time1, time2 = time2)
        val hours = Math.abs((totalTime) / 1000 / 60 / 60).toString()
        var minutes = Math.abs((totalTime) / 1000 / 60 % 60).toString()
        if (minutes.length % 2 != 0)
            minutes = "0$minutes"
        return "$hours:$minutes"
    }

}