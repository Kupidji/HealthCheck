package com.example.healthcheck.ui.medicine

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentAddMedicinesBinding
import com.example.healthcheck.models.MedicineParams
import com.example.healthcheck.util.animations.ButtonPress.buttonPressAnimation
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.medicine.AddMedicinesViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar

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

        binding.getFirstTime.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.getFirstTime.hideKeyboard()
                setTimePicker(binding.getFirstTime) { callback ->
                    firstTime = callback
                    onGetTime(binding.getFirstTime)
                }
            }
        }

        binding.getFirstTime.setOnClickListener {
            binding.getFirstTime.hideKeyboard()
            setTimePicker(binding.getFirstTime) { callback ->
                firstTime = callback
                onGetTime(binding.getFirstTime)
            }

        }

        binding.textInputLayout2.setEndIconOnClickListener {
            onClearTextClick(binding.textInputLayout2)
        }

        binding.getSecondTime.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.getSecondTime.hideKeyboard()
                setTimePicker(binding.getSecondTime) { callback ->
                    secondTime = callback
                    onGetTime(binding.getSecondTime)
                }
            }
        }

        binding.getSecondTime.setOnClickListener {
            binding.getSecondTime.hideKeyboard()
            setTimePicker(binding.getSecondTime) { callback ->
                secondTime = callback
                onGetTime(binding.getSecondTime)
            }

        }

        binding.textInputLayout3.setEndIconOnClickListener {
            onClearTextClick(binding.textInputLayout3)
        }

        binding.getThirdTime.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.getThirdTime.hideKeyboard()
                setTimePicker(binding.getThirdTime) { callback ->
                    thirdTime = callback
                    onGetTime(binding.getThirdTime)
                }
            }
        }

        binding.getThirdTime.setOnClickListener {
            binding.getThirdTime.hideKeyboard()
            setTimePicker(binding.getThirdTime) { callback ->
                thirdTime = callback
                onGetTime(binding.getThirdTime)
            }
        }

        binding.textInputLayout4.setEndIconOnClickListener {
            onClearTextClick(binding.textInputLayout4)
        }

        binding.getFourthTime.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.getFourthTime.hideKeyboard()
                setTimePicker(binding.getFourthTime) { callback ->
                    fourthTime = callback
                    onGetTime(binding.getFourthTime)
                }
            }
        }

        binding.getFourthTime.setOnClickListener {
            binding.getFourthTime.hideKeyboard()
            setTimePicker(binding.getFourthTime) { callback ->
                fourthTime = callback
                onGetTime(binding.getFourthTime)
            }
        }

        binding.textInputLayout5.setEndIconOnClickListener {
            onClearTextClick(binding.textInputLayout5)
        }

        binding.wentBack.setOnClickListener {
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.medicinesFragment, true)
                .build()
            //навигация и анимации
            val direction =
                addMedicinesFragmentDirections.actionAddMedicinesFragmentToMedicinesFragment()
            val navigate = {nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        binding.saveMedicine.setOnClickListener {
            if (binding.getTitle.text.toString().isNotEmpty()) {
                binding.textInputLayout.error = null
                var durationOfCourse = 0
                if (binding.getCountOfDays.text.toString().isNotEmpty()) {
                    durationOfCourse = binding.getCountOfDays.text.toString().toInt()
                }

                //создание сущности в таблице medicines и создание уведомлений(во viewModel)
                viewModel.insertMedicine(
                    MedicineParams(
                        title = binding.getTitle.text.toString(),
                        durationOfCourse = durationOfCourse,
                        timeOfNotify1 = firstTime,
                        timeOfNotify2 = secondTime,
                        timeOfNotify3 = thirdTime,
                        timeOfNotify4 = fourthTime,
                    )
                )

                val direction = addMedicinesFragmentDirections.actionAddMedicinesFragmentToMedicinesFragment()
                var navOptions = NavOptions.Builder()
                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                    .setPopUpTo(R.id.medicinesFragment, true)
                    .build()
                val navigate = {nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
                buttonChangeScreenAnimation(binding.saveMedicine, navigation, direction, navOptions, navigate)
                Toast.makeText(this@addMedicinesFragment.requireContext(), "Курс ${binding.getTitle.text.toString()} был создан", Toast.LENGTH_SHORT).show()
            }
            else {
                buttonPressAnimation(binding.saveMedicineLayout)
                binding.textInputLayout.error = this.requireContext().getString(R.string.mandatoryValue)
            }

        }

    }

    private fun onGetTime(textView: TextView) {
        when (textView) {

            binding.getFirstTime -> {
                addAnimation(binding.secondTimeBox)
            }

            binding.getSecondTime -> {
                addAnimation(binding.thirdTimeBox)
            }

            binding.getThirdTime -> {
                addAnimation(binding.fourthTimeBox)
            }

        }
    }

    private fun onClearTextClick(view : TextInputLayout) {

        when (view) {

            binding.textInputLayout2 -> {
                binding.textInputLayout2.editText?.text?.clear()
                firstTime = 0L

                if (binding.getSecondTime.text.toString().isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                }
                if (binding.getSecondTime.text.toString().isEmpty() && binding.getThirdTime.text.toString().isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                    deleteAnimation(binding.thirdTimeBox)
                }
                if (binding.getSecondTime.text.toString().isEmpty() && binding.getThirdTime.text.toString().isEmpty() && binding.getFourthTime.text.toString().isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                    deleteAnimation(binding.thirdTimeBox)
                    deleteAnimation(binding.fourthTimeBox)
                }
            }

            binding.textInputLayout3 -> {
                binding.textInputLayout3.editText?.text?.clear()
                secondTime = 0L

                if (binding.getThirdTime.text.toString().isEmpty()) {
                    deleteAnimation(binding.thirdTimeBox)
                }
                if (binding.getFirstTime.text.toString().isEmpty() && binding.getThirdTime.text.toString().isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                    deleteAnimation(binding.thirdTimeBox)
                }
                if (binding.getFirstTime.text.toString().isEmpty() && binding.getThirdTime.text.toString().isEmpty() && binding.getFourthTime.text.toString().isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                    deleteAnimation(binding.thirdTimeBox)
                    deleteAnimation(binding.fourthTimeBox)
                }
            }

            binding.textInputLayout4 -> {
                binding.textInputLayout4.editText?.text?.clear()
                thirdTime = 0L

                if (binding.getFourthTime.text.toString().isEmpty()) {
                    deleteAnimation(binding.fourthTimeBox)
                }
                if (binding.getSecondTime.text.toString().isEmpty() && binding.getFourthTime.text.toString().isEmpty()) {
                    deleteAnimation(binding.thirdTimeBox)
                    deleteAnimation(binding.fourthTimeBox)
                }
                if (binding.getFirstTime.text.toString().isEmpty() && binding.getThirdTime.text.toString().isEmpty() && binding.getFourthTime.text.toString().isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                    deleteAnimation(binding.thirdTimeBox)
                    deleteAnimation(binding.fourthTimeBox)
                }
            }

            binding.textInputLayout5 -> {
                binding.textInputLayout5.editText?.text?.clear()
                fourthTime = 0L

                if (binding.getFirstTime.text.toString().isEmpty() && binding.getThirdTime.text.toString().isEmpty()) {
                    deleteAnimation(binding.secondTimeBox)
                    deleteAnimation(binding.thirdTimeBox)
                }
            }

        }

    }

    private fun addAnimation(view: ConstraintLayout) {
        view.visibility = View.VISIBLE
        view.alpha = 0F
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
                .setTitleText(this@addMedicinesFragment.context?.getString(R.string.timeOfNotification))
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

    private fun EditText.hideKeyboard() {
        if (requestFocus()) {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(this.windowToken, 0)
            setSelection(text.length)
        }
    }

}