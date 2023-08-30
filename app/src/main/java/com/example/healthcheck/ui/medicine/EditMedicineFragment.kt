package com.example.healthcheck.ui.medicine

import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.models.Medicines
import com.example.healthcheck.R
import com.example.healthcheck.viewmodels.medicine.EditMedicineViewModel
import com.example.healthcheck.databinding.FragmentEditMedicineBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class editMedicineFragment : Fragment() {

    companion object {
        fun newInstance() = editMedicineFragment()
    }

    private lateinit var viewModel: EditMedicineViewModel
    private lateinit var binding : FragmentEditMedicineBinding
    private val args : editMedicineFragmentArgs by navArgs()
    private var firstTime : Long = 0L
    private var secondTime : Long = 0L
    private var thirdTime : Long = 0L
    private var fourthTime : Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditMedicineBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(EditMedicineViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        firstTime = args.firstTimeNotification
        secondTime = args.secondTimeNotification
        thirdTime = args.thirdTimeNotification
        fourthTime = args.fourthTimeNotification

        binding.getTitle.setText(args.title)

        if (args.firstTimeNotification != 0L) {
            binding.getFirstTime.setText(SimpleDateFormat("HH:mm").format(args.firstTimeNotification))
            binding.deleteFirstTime.visibility = View.VISIBLE
            binding.secondTimeBox.visibility = View.VISIBLE
        }

        if (args.secondTimeNotification != 0L) {
            binding.secondTimeBox.visibility = View.VISIBLE
            binding.deleteSecondTime.visibility = View.VISIBLE
            binding.getSecondTime.setText(SimpleDateFormat("HH:mm").format(args.secondTimeNotification))
            binding.thirdTimeBox.visibility = View.VISIBLE
        }

        if (args.thirdTimeNotification != 0L) {
            binding.thirdTimeBox.visibility = View.VISIBLE
            binding.deleteThirdTime.visibility = View.VISIBLE
            binding.getThirdTime.setText(SimpleDateFormat("HH:mm").format(args.thirdTimeNotification))
            binding.fourthTimeBox.visibility = View.VISIBLE
        }

        if (args.fourthTimeNotification != 0L) {
            binding.fourthTimeBox.visibility = View.VISIBLE
            binding.deleteFourthTime.visibility = View.VISIBLE
            binding.getFourthTime.setText(SimpleDateFormat("HH:mm").format(args.fourthTimeNotification))
        }

        if (args.totalDuractionOfCourse != 0) {
            binding.getCountOfDays.setText(args.totalDuractionOfCourse.toString())
        }

        binding.getFirstTime.setOnClickListener {
            setTimePicker(binding.getFirstTime) { callback ->
                firstTime = callback
                onGetTime(binding.getFirstTime)
            }
        }

        binding.deleteFirstTime.setOnClickListener {
            onDeleteTime(binding.deleteFirstTime)
        }

        binding.getSecondTime.setOnClickListener {
            setTimePicker(binding.getSecondTime) { callback ->
                secondTime = callback
                onGetTime(binding.getSecondTime)
            }
        }

        binding.deleteSecondTime.setOnClickListener {
            onDeleteTime(binding.deleteSecondTime)
        }

        binding.getThirdTime.setOnClickListener {
            setTimePicker(binding.getThirdTime) { callback ->
                thirdTime = callback
                onGetTime(binding.getThirdTime)
            }
        }

        binding.deleteThirdTime.setOnClickListener {
            onDeleteTime(binding.deleteThirdTime)
        }

        binding.getFourthTime.setOnClickListener {
            setTimePicker(binding.getFourthTime) { callback ->
                fourthTime = callback
                onGetTime(binding.getFourthTime)
            }
        }

        binding.deleteFourthTime.setOnClickListener {
            onDeleteTime(binding.deleteFourthTime)
        }

        //навигация и анимация
        binding.wentBack.setOnClickListener {
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.medicinesFragment, true)
                .build()
            val direction = editMedicineFragmentDirections.actionEditMedicineFragmentToMedicinesFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        binding.saveChangeMedicine.setOnClickListener {

            if (binding.getTitle.text.isNotEmpty()) {

                var durationOfCourse: Int
                if (binding.getCountOfDays.text.isNotEmpty()) {
                    durationOfCourse = binding.getCountOfDays.text.toString().toInt()
                } else {
                    durationOfCourse = 0
                }

                val medicines = Medicines(
                    id = args.id,
                    title = binding.getTitle.text.toString(),
                    dateStart = args.dateOfStart,
                    durationOfCourse = durationOfCourse,
                    currentDayOfCourse = args.currentDay,
                    timeOfNotify1 = firstTime,
                    channelIDFirstTime = args.firstTimeChannelID,
                    timeOfNotify2 = secondTime,
                    channelIDSecondTime = args.secondTimeChannelID,
                    timeOfNotify3 = thirdTime,
                    channelIDThirdTime = args.thirdTimeChannelID,
                    timeOfNotify4 = fourthTime,
                    channelIDFourthTime = args.fourthTimeChannelID,
                    totalMissed = 0,
                )

                viewModel.updateMedicine(medicines)

              //если время или название изменено, удаление прежних уведомлений, создание новых
                if (firstTime != args.firstTimeNotification && firstTime != 0L || args.title != medicines.title && firstTime != 0L) {
                    viewModel.updateNotifications(
                        args.firstTimeNotification,
                        "Первый приём - " + args.title,
                        args.firstTimeChannelID
                    )

                }

                if (firstTime == 0L) {
                    viewModel.updateNotifications(
                        args.firstTimeNotification,
                        "Первый приём - " + args.title,
                        args.firstTimeChannelID
                    )
                }

                if (secondTime != args.secondTimeNotification && secondTime != 0L || args.title != medicines.title && secondTime != 0L) {
                    viewModel.updateNotifications(
                        args.secondTimeNotification,
                        "Второй приём - " + args.title,
                        args.secondTimeChannelID
                    )

                }

                if (secondTime == 0L) {
                    viewModel.updateNotifications(
                        args.secondTimeNotification,
                        "Второй приём - " + args.title,
                        args.secondTimeChannelID
                    )
                }

                if (thirdTime != args.thirdTimeNotification && thirdTime != 0L || args.title != medicines.title && thirdTime != 0L) {
                    viewModel.updateNotifications(
                        args.thirdTimeNotification,
                        "Третий приём - " + args.title,
                        args.thirdTimeChannelID
                    )

                }

                if (thirdTime == 0L) {
                    viewModel.updateNotifications(
                        args.thirdTimeNotification,
                        "Третий приём - " + args.title,
                        args.thirdTimeChannelID
                    )
                }


                if (fourthTime != args.fourthTimeNotification && fourthTime != 0L || args.title != medicines.title && fourthTime != 0L ) {
                    viewModel.updateNotifications(
                        args.fourthTimeNotification,
                        "Четвёртый приём - " + args.title,
                        args.fourthTimeChannelID
                    )
                }

                if (fourthTime == 0L) {
                    viewModel.updateNotifications(
                        args.fourthTimeNotification,
                        "Четвёртый приём - " + args.title,
                        args.fourthTimeChannelID
                    )
                }

                Toast.makeText(this@editMedicineFragment.requireContext(), "Изменения были внесены", Toast.LENGTH_SHORT).show()
                //навигация и анимации
                var navOptions = NavOptions.Builder()
                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                    .setPopUpTo(R.id.medicinesFragment, true)
                    .build()
                val direction = editMedicineFragmentDirections.actionEditMedicineFragmentToMedicinesFragment()
                val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
                buttonChangeScreenAnimation(binding.saveChangeMedicineLayout, navigation, direction, navOptions, navigate)
            }
            else {
                binding.saveChangeMedicineLayout.animate()
                    .setDuration(75)
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .withEndAction {
                        binding.saveChangeMedicineLayout.animate()
                            .setDuration(25)
                            .scaleX(1f)
                            .scaleY(1f)
                    }
                binding.getTitle.error = "Обязательное поле"
            }
        }
    }

    private fun onGetTime(textView: TextView) {
        when (textView) {

            binding.getFirstTime -> {
                addAnimation(binding.secondTimeBox)
                binding.deleteFirstTime.visibility = View.VISIBLE
            }

            binding.getSecondTime -> {
                addAnimation(binding.thirdTimeBox)
                binding.deleteSecondTime.visibility = View.VISIBLE
            }

            binding.getThirdTime -> {
                addAnimation(binding.fourthTimeBox)
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
                firstTime = 0L
                binding.deleteFirstTime.visibility = View.GONE

                if (binding.getSecondTime.text.isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                }
                if (binding.getSecondTime.text.isEmpty() && binding.getThirdTime.text.isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                    deleteAnimation(binding.thirdTimeBox)
                }
                if (binding.getSecondTime.text.isEmpty() && binding.getThirdTime.text.isEmpty() && binding.getFourthTime.text.isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                    deleteAnimation(binding.thirdTimeBox)
                    deleteAnimation(binding.fourthTimeBox)
                }


            }

            binding.deleteSecondTime -> {
                binding.getSecondTime.text = ""
                secondTime = 0L
                binding.deleteSecondTime.visibility = View.GONE

                if (binding.getThirdTime.text.isEmpty()) {
                    deleteAnimation(binding.thirdTimeBox)
                }
                if (binding.getFirstTime.text.isEmpty() && binding.getThirdTime.text.isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                    deleteAnimation(binding.thirdTimeBox)
                }
                if (binding.getFirstTime.text.isEmpty() && binding.getThirdTime.text.isEmpty() && binding.getFourthTime.text.isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                    deleteAnimation(binding.thirdTimeBox)
                    deleteAnimation(binding.fourthTimeBox)
                }

            }

            binding.deleteThirdTime -> {
                binding.getThirdTime.text = ""
                thirdTime = 0L
                binding.deleteThirdTime.visibility = View.GONE

                if (binding.getFourthTime.text.isEmpty()) {
                    deleteAnimation(binding.fourthTimeBox)
                }
                if (binding.getSecondTime.text.isEmpty() && binding.getFourthTime.text.isEmpty()) {
                    deleteAnimation(binding.thirdTimeBox)
                    deleteAnimation(binding.fourthTimeBox)
                }
                if (binding.getFirstTime.text.isEmpty() && binding.getThirdTime.text.isEmpty() && binding.getFourthTime.text.isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                    deleteAnimation(binding.thirdTimeBox)
                    deleteAnimation(binding.fourthTimeBox)
                }

            }

            binding.deleteFourthTime -> {
                binding.getFourthTime.text = ""
                fourthTime = 0L
                binding.deleteFourthTime.visibility = View.GONE

                if (binding.getFirstTime.text.isEmpty() && binding.getThirdTime.text.isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                    deleteAnimation(binding.thirdTimeBox)
                }
            }

        }
    }

    private fun addAnimation(view: ConstraintLayout) {
        view.visibility = View.VISIBLE
        view.animate()
            .setDuration(300L)
            .scaleX(1F)
            .scaleY(1F)
            .alpha(1F)
    }
    private fun deleteAnimation(view: ConstraintLayout) {
        view.animate()
            .setDuration(300L)
            .scaleX(0.95F)
            .scaleY(0.95F)
            .alpha(0F)
            .withEndAction {
                view.visibility = View.GONE
            }
    }

    private fun setTimePicker(textView: TextView, callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(this.get(Calendar.HOUR_OF_DAY))
                .setMinute(this.get(Calendar.MINUTE))
                .setTitleText(this@editMedicineFragment.context?.getString(R.string.timeOfNotification))
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                this.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                this.set(Calendar.MINUTE, timePicker.minute)
                callback(this.timeInMillis)
                textView.text = SimpleDateFormat("HH:mm").format(this.time)
            }

            timePicker.show(requireActivity().supportFragmentManager, "timePicker")
        }
    }

}