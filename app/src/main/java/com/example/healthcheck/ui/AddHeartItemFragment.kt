package com.example.healthcheck.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
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

        when (value) {
            in 30..230 -> {
                return true
            }

            else -> {
                editText.error = context?.getString(R.string.wrongValue)
                return false
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

}