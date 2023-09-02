package com.example.healthcheck.ui.start

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentStartFragment7Binding
import com.example.healthcheck.notifications.service.NotificationService
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.start.StartFragment7ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class StartFragment7 : Fragment() {

    companion object {
        fun newInstance() = StartFragment7()
    }

    private lateinit var viewModel: StartFragment7ViewModel
    private lateinit var binding : FragmentStartFragment7Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartFragment7Binding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(StartFragment7ViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        val navigation = findNavController()

        val numberPicker1: NumberPicker = binding.numberPicker1
        numberPicker1.maxValue = 200
        numberPicker1.minValue = 20
        numberPicker1.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPicker1.wrapSelectorWheel = false
        binding.getWeight.setText(numberPicker1.value.toString())

        numberPicker1.setOnValueChangedListener { picker, oldVal, newVal ->
            binding.getWeight.setText(newVal.toString())
        }

        binding.next.setOnClickListener {
            if (binding.getWeight.text.toString().isNotEmpty()) {
                if (binding.getWeight.text.toString().toFloat() in 1.0..635.0) {
                    viewModel.insertWeight(binding.getWeight.text.toString().toFloat())
                    binding.textInputLayout.error = null

                    viewModel.setFirstLaunchCompleted()

                    //TODO убрать в некст обновлении
                    val settings = context?.applicationContext?.getSharedPreferences(Constants.IS_FIRST_TIME, Context.MODE_PRIVATE)?.edit()
                    settings?.putBoolean(Constants.IS_FIRST_TIME, false)?.apply()

                    //Уведомления для напоминаний о вводе данных
                    var service = NotificationService(this.requireContext())

                    val time = Calendar.getInstance()
                    time.timeInMillis = System.currentTimeMillis()
                    time[Calendar.HOUR_OF_DAY] = 20
                    time[Calendar.MINUTE] = 0
                    time[Calendar.SECOND] = 1

                    viewModel.setTimeOfRegularNotification(time.timeInMillis)
                    service.setRepetitiveAlarm(time.timeInMillis, Constants.REGULAR_MESSAGE, Constants.REGULAR_CHANNEL_ID)
                    Log.d("Notification", "onViewCreated: ${SimpleDateFormat("HH:mm").format(time.timeInMillis)}")

                    val direction = StartFragment7Directions.actionStartFragment7ToMainFragment()
                    val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
                    buttonChangeScreenAnimation(binding.next, navigation, direction, navOptions, navigate)
                }
                else {
                    binding.textInputLayout.error = this.requireContext().getString(R.string.wrongValue)
                }

            }
            else {
                binding.textInputLayout.error = this.requireContext().getString(R.string.mandatoryValue)
            }

        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })

    }

}