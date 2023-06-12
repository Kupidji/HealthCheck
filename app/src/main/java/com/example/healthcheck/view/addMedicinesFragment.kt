package com.example.healthcheck.view

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentAddMedicinesBinding
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.util.RandomUtil
import com.example.healthcheck.viewmodel.AddMedicinesViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class addMedicinesFragment : Fragment() {

    companion object {
        fun newInstance() = addMedicinesFragment()
    }

    private lateinit var viewModel: AddMedicinesViewModel
    private lateinit var binding : FragmentAddMedicinesBinding
    private var firstTime : Long = 0L
    private var secondTime : Long = 0L
    private var thirdTime : Long = 0L
    private var fourthTime : Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AddMedicinesViewModel::class.java)
        binding = FragmentAddMedicinesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        binding.wentBack.setOnClickListener {
            navigation.navigate(R.id.medicinesFragment)
        }

        binding.profile.setOnClickListener {
            navigation.navigate(R.id.profileFragment)
        }

        binding.getFirstTime.setOnClickListener {
            setAlarm(binding.getFirstTime) { callback ->
                firstTime = callback
                onGetTime(binding.getFirstTime)
            }
        }

        binding.deleteFirstTime.setOnClickListener {
            onDeleteTime(binding.deleteFirstTime)
        }

        binding.getSecondTime.setOnClickListener {
            setAlarm(binding.getSecondTime) { callback ->
                secondTime = callback
                onGetTime(binding.getSecondTime)
            }
        }

        binding.deleteSecondTime.setOnClickListener {
            onDeleteTime(binding.deleteSecondTime)
        }

        binding.getThirdTime.setOnClickListener {
            setAlarm(binding.getThirdTime) { callback ->
                thirdTime = callback
                onGetTime(binding.getThirdTime)
            }
        }

        binding.deleteThirdTime.setOnClickListener {
            onDeleteTime(binding.deleteThirdTime)
        }

        binding.getFourthTime.setOnClickListener {
            setAlarm(binding.getFourthTime) { callback ->
                fourthTime = callback
                onGetTime(binding.getFourthTime)
            }
        }

        binding.deleteFourthTime.setOnClickListener {
            onDeleteTime(binding.deleteFourthTime)
        }

        binding.saveMedicine.setOnClickListener {
            if (binding.getTitle.text.isNotEmpty()) {

                lifecycleScope.launch {
                    var currentDate = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date())

                    var durationOfCourse : Int
                    if (binding.getCountOfDays.text.isNotEmpty()) {
                        durationOfCourse = binding.getCountOfDays.text.toString().toInt()
                    }
                    else {
                        durationOfCourse = 0
                    }

                    val medicines = Medicines (
                        id = 0,
                        title = binding.getTitle.text.toString(),
                        dateStart = currentDate,
                        durationOfCourse = durationOfCourse,
                        currentDayOfCourse = 1,
                        timeOfNotify1 = firstTime,
                        channelIDFirstTime = RandomUtil.getRandomInt(),
                        timeOfNotify2 = secondTime,
                        channelIDSecondTime = RandomUtil.getRandomInt(),
                        timeOfNotify3 = thirdTime,
                        channelIDThirdTime = RandomUtil.getRandomInt(),
                        timeOfNotify4 = fourthTime,
                        channelIDFourthTime = RandomUtil.getRandomInt(),
                        totalMissed = 0,
                    )
                    //создание уведомлений
                    viewModel.createNotification (
                        medicines.timeOfNotify1,
                        "Первый приём - " + medicines.title,
                        medicines.channelIDFirstTime
                    )
                    viewModel.createNotification (
                        medicines.timeOfNotify2,
                        "Второй приём - " + medicines.title,
                        medicines.channelIDSecondTime
                    )
                    viewModel.createNotification (
                        medicines.timeOfNotify3,
                        "Третий приём - " + medicines.title,
                        medicines.channelIDThirdTime
                    )
                    viewModel.createNotification (
                        medicines.timeOfNotify4,
                        "Четвёртый приём - " + medicines.title,
                        medicines.channelIDFourthTime
                    )

                    viewModel.createMedicine(medicines)
                    navigation.navigate(R.id.medicinesFragment)
                }

            }
            else {
                binding.getTitle.error = "Обязательное поле"
            }

        }

    }

    private fun onGetTime(textView: TextView) {
        when (textView) {

            binding.getFirstTime -> {
                binding.secondTimeBox.visibility = View.VISIBLE
                binding.deleteFirstTime.visibility = View.VISIBLE
            }

            binding.getSecondTime -> {
                binding.thirdTimeBox.visibility = View.VISIBLE
                binding.deleteSecondTime.visibility = View.VISIBLE
            }

            binding.getThirdTime -> {
                binding.fourthTimeBox.visibility = View.VISIBLE
                binding.deleteThirdTime.visibility = View.VISIBLE
            }

            binding.getFourthTime -> {
                binding.deleteFourthTime.visibility = View.VISIBLE
            }

        }
    }

    private fun onDeleteTime(imageView: ImageView) {

        when (imageView) {
            binding.deleteFirstTime -> {
                binding.getFirstTime.text = ""
                binding.deleteFirstTime.visibility = View.GONE

                if (binding.getSecondTime.text.isEmpty()) {
                    binding.secondTimeBox.visibility = View.GONE
                }
                if (binding.getSecondTime.text.isEmpty() && binding.getThirdTime.text.isEmpty()) {
                    binding.secondTimeBox.visibility = View.GONE
                    binding.thirdTimeBox.visibility = View.GONE
                }
                if(binding.getSecondTime.text.isEmpty() && binding.getThirdTime.text.isEmpty() && binding.getFourthTime.text.isEmpty()) {
                    binding.secondTimeBox.visibility = View.GONE
                    binding.thirdTimeBox.visibility = View.GONE
                    binding.fourthTimeBox.visibility = View.GONE
                }


            }

            binding.deleteSecondTime -> {
                binding.getSecondTime.text = ""
                binding.deleteSecondTime.visibility = View.GONE

                if (binding.getThirdTime.text.isEmpty()) {
                    binding.thirdTimeBox.visibility = View.GONE
                }
                if (binding.getFirstTime.text.isEmpty() && binding.getThirdTime.text.isEmpty()) {
                    binding.secondTimeBox.visibility = View.GONE
                    binding.thirdTimeBox.visibility = View.GONE
                }
                if (binding.getFirstTime.text.isEmpty() && binding.getThirdTime.text.isEmpty() && binding.getFourthTime.text.isEmpty()) {
                    binding.secondTimeBox.visibility = View.GONE
                    binding.thirdTimeBox.visibility = View.GONE
                    binding.fourthTimeBox.visibility = View.GONE
                }

            }

            binding.deleteThirdTime -> {
                binding.getThirdTime.text = ""
                binding.deleteThirdTime.visibility = View.GONE

                if (binding.getFourthTime.text.isEmpty()) {
                    binding.fourthTimeBox.visibility = View.GONE
                }
                if (binding.getSecondTime.text.isEmpty() && binding.getFourthTime.text.isEmpty()) {
                    binding.thirdTimeBox.visibility = View.GONE
                    binding.fourthTimeBox.visibility = View.GONE
                }
                if (binding.getFirstTime.text.isEmpty() && binding.getThirdTime.text.isEmpty() && binding.getFourthTime.text.isEmpty()) {
                    binding.secondTimeBox.visibility = View.GONE
                    binding.thirdTimeBox.visibility = View.GONE
                    binding.fourthTimeBox.visibility = View.GONE
                }

            }

            binding.deleteFourthTime -> {
                binding.getFourthTime.text = ""
                binding.deleteFourthTime.visibility = View.GONE

                if (binding.getFirstTime.text.isEmpty() && binding.getThirdTime.text.isEmpty()) {
                    binding.secondTimeBox.visibility = View.GONE
                    binding.thirdTimeBox.visibility = View.GONE
                }
            }

        }
    }
    private fun setAlarm(textView: TextView, callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            TimePickerDialog(
                this@addMedicinesFragment.context,
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