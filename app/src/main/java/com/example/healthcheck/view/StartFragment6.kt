package com.example.healthcheck.view

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
import com.example.healthcheck.databinding.FragmentStart6Binding
import com.example.healthcheck.model.global_notifications.service.NotificationService
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodel.StartFragment6ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar


class StartFragment6 : Fragment() {

    companion object {
        fun newInstance() = StartFragment6()
    }

    private lateinit var viewModel: StartFragment6ViewModel
    private lateinit var binding : FragmentStart6Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(StartFragment6ViewModel::class.java)
        binding = FragmentStart6Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        val numberPicker2: NumberPicker = binding.numberPicker2
        numberPicker2.setMaxValue(300)
        numberPicker2.setMinValue(100)
        numberPicker2.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPicker2.setWrapSelectorWheel(false)

        binding.next.setOnClickListener {
            val editorforname = viewModel.settings.edit()
            editorforname?.putFloat(Constants.HEIGHT_START, binding.numberPicker2.value.toString().toFloat())?.apply()

            //навигация и анимации
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.mainFragment, true)
                .build()
            var direction = StartFragment6Directions.actionStartFragment6ToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.next, navigation, direction, navOptions, navigate)

            var settings =this@StartFragment6.context?.applicationContext?.getSharedPreferences(Constants.IS_FIRST_TIME, Context.MODE_PRIVATE)
            val editor = settings?.edit()
            editor?.putBoolean(
                Constants.IS_FIRST_TIME,
                false
            )?.apply()

            //Уведомления для напоминаний о вводе данных
            var service = NotificationService(this.requireContext())

            val time = Calendar.getInstance()
            time.timeInMillis = System.currentTimeMillis()
            time[Calendar.HOUR_OF_DAY] = 18
            time[Calendar.MINUTE] = 0
            time[Calendar.SECOND] = 1

            Log.d("Notification", "onViewCreated: ${SimpleDateFormat("HH:mm").format(time.timeInMillis)}")
            val settingsNotification = context?.applicationContext?.getSharedPreferences(Constants.TIME_OF_NOTIFICATION, Context.MODE_PRIVATE)
            val editorNotification = settingsNotification?.edit()
            service.setRepetitiveAlarm(time.timeInMillis, Constants.REGULAR_MESSAGE, Constants.REGULAR_CHANNEL_ID)
            editorNotification?.putLong(Constants.TIME_OF_NOTIFICATION, time.timeInMillis)?.apply()
            Log.d("Notification", "onViewCreated: ${SimpleDateFormat("HH:mm").format(settingsNotification?.getLong(Constants.TIME_OF_NOTIFICATION, 0))}")
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })
    }

}