package com.example.healthcheck.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentAddHeartItemBinding
import com.example.healthcheck.models.HeartItemParams
import com.example.healthcheck.util.animations.ButtonPress.buttonPressAnimation
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.heart.AddHeartItemViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddHeartItemFragment : Fragment() {

    companion object {
        fun newInstance() = AddHeartItemFragment()
    }

    private lateinit var viewModel : AddHeartItemViewModel
    private lateinit var binding : FragmentAddHeartItemBinding
    private var _date = Calendar.getInstance().timeInMillis


    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AddHeartItemViewModel::class.java)
        binding = FragmentAddHeartItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        binding.upPressureBox.showKeyboard()
        binding.upPressureBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    if (s.toString().startsWith("1") || s.toString().startsWith("2")) {
                        if (s.toString().length == 3) {
                            binding.downPressureBox.showKeyboard()
                        }
                    }
                    else {
                        if (s.toString().length == 2) {
                            binding.downPressureBox.showKeyboard()
                        }
                    }
                }
                catch (e : StringIndexOutOfBoundsException) {

                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.downPressureBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    if (s.toString().startsWith("1") || s.toString().startsWith("2")) {
                        if (s.toString().length == 3) {
                            binding.pulseBox.showKeyboard()
                        }
                    }
                    else {
                        if (s.toString().length == 2) {
                            binding.pulseBox.showKeyboard()
                        }
                    }
                }
                catch (e : StringIndexOutOfBoundsException) {

                }

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    binding.upPressureBox.showKeyboard()
                }
            }

        })

        binding.pulseBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    if (s.toString().startsWith("1") || s.toString().startsWith("2")) {
                        if (s.toString().length == 3) {
                            binding.pulseBox.hideKeyboard()
                        }
                    }
                    else {
                        if (s.toString().length == 2) {
                            binding.pulseBox.hideKeyboard()
                        }
                    }
                }
                catch (e : StringIndexOutOfBoundsException) {

                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    binding.downPressureBox.showKeyboard()
                }
            }

        })

        binding.dateAndTimeText.text = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(_date)

        binding.dateAndTimeBoxLayout.setOnClickListener {
            setAlarm(textView = binding.dateAndTimeText) { callback ->
                _date = callback
            }
        }

        binding.wentBack.setOnClickListener {
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.heartFragment, true)
                .build()
            //навигация и анимации
            val direction =
                AddHeartItemFragmentDirections.actionAddHeartItemFragmentToHeartFragment()
            val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        binding.saveHeartItemBtn.setOnClickListener {
            buttonPressAnimation(binding.addHeartItemLayout)
            if (
                checkError(editText = binding.upPressureBox, binding.upPressureBox.text.toString()) &&
                checkError(editText = binding.downPressureBox,binding.downPressureBox.text.toString()) &&
                checkError(editText = binding.pulseBox, binding.pulseBox.text.toString())
            ) {
                viewModel.insertHeartItem(
                    HeartItemParams(
                        pressureUp = binding.upPressureBox.text.toString().toInt(),
                        pressureDown = binding.downPressureBox.text.toString().toInt(),
                        pulse = binding.pulseBox.text.toString().toInt(),
                        date = _date
                    )
                )

                var navOptions = NavOptions.Builder()
                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                    .setPopUpTo(R.id.heartFragment, true)
                    .build()
                //навигация и анимации
                val direction =
                    AddHeartItemFragmentDirections.actionAddHeartItemFragmentToHeartFragment()
                navigation.navigate(direction, navOptions)
            }

        }

    }

    private fun checkError(editText : EditText, string : String) : Boolean {
        var value: Int
        if (string.isNotEmpty()) {
            value = string.toInt()
        }
        else {
            editText.error = context?.getString(R.string.emptyString)
            return false
        }

        return when (value) {
            in 30..230 -> {
                true
            }

            else -> {
                editText.error = context?.getString(R.string.wrongValue)
                false
            }
        }

    }

    private fun setAlarm(textView: TextView, callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog(
                this@AddHeartItemFragment.requireContext(),
                { _, year, month, dayOfMonth ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    TimePickerDialog(
                        this@AddHeartItemFragment.context,
                        0,
                        { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            callback(this.timeInMillis)
                            textView.text = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(this.time)
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()

        }
    }

    private fun EditText.showKeyboard() {
        if (requestFocus()) {
            (activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(this, SHOW_IMPLICIT)
            setSelection(text.length)
        }
    }

    private fun EditText.hideKeyboard() {
        if (requestFocus()) {
            (activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(this.windowToken, 0)
            setSelection(text.length)
        }
    }

}