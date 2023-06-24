package com.example.healthcheck.view

import android.graphics.Rect
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentHeartBinding
import com.example.healthcheck.model.heart.entities.Heart
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodel.HeartViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class heartFragment : Fragment() {

    companion object {
        fun newInstance() = heartFragment()
    }

    private lateinit var viewModel: HeartViewModel
    private lateinit var binding : FragmentHeartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HeartViewModel::class.java)
        binding = FragmentHeartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        viewModel.upperPressure.observe(this@heartFragment.viewLifecycleOwner) {
            binding.ur1.text = it.toString()
        }
        viewModel.lowerPressure.observe(this@heartFragment.viewLifecycleOwner) {
            binding.ur2.text = it.toString()
        }
        viewModel.pulse.observe(this@heartFragment.viewLifecycleOwner) {
            binding.ur3.text = it.toString()
        }

        binding.wentBack.setOnClickListener {

            if ((binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty()) ||
                (binding.getUpPressure.text.isEmpty() && binding.getDownPressure.text.isEmpty() && binding.getPulse.text.isEmpty())) {

                //навигация и анимации
                val direction = heartFragmentDirections.actionHeartFragmentToMainFragment()
                val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
                buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
                saveHeartSettings(binding.ur1.text.toString() + "/" + binding.ur2.text.toString())

            }
            else {
                exit(binding.getUpPressure)
                exit(binding.getDownPressure)
                exit(binding.getPulse)
            }

        }

        //навигация и анимации
        binding.profile.setOnClickListener {
            if ((binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty()) ||
                (binding.getUpPressure.text.isEmpty() && binding.getDownPressure.text.isEmpty() && binding.getPulse.text.isEmpty())) {

                    val direction = heartFragmentDirections.actionHeartFragmentToProfileFragment()
                    val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
                    buttonChangeScreenAnimation(binding.profile, navigation, direction, navOptions, navigate)
            } else {
                exit(binding.getUpPressure)
                exit(binding.getDownPressure)
                exit(binding.getPulse)
            }
        }



        view.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            val heightDiff = view.rootView.height - r.height()

            //Если клавиатура убрана
            if (heightDiff < 0.2 * view.rootView.height) {
                binding.getPulse.hasFocus()
                binding.getUpPressure.hasFocus()
                binding.getDownPressure.hasFocus()
                forFocus(false)


                //
                afterKeyboardIsDown(binding.getUpPressure, Constants.UPPER)
                afterKeyboardIsDown(binding.getDownPressure, Constants.LOWER)
                afterKeyboardIsDown(binding.getPulse, Constants.PULSE)

            } else {
                forFocus(true)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveHeartSettings(binding.ur1.text.toString() + "/" + binding.ur2.text.toString())
    }

    private fun afterKeyboardIsDown(editText: EditText, key: String, ) {

        if (editText.text.isNotEmpty() && editText.isFocused) {
            afterKeyboardClosedOrLostFocusForHeart(editText, key)

        }
        else if (editText.text.isEmpty() && editText.isFocused){
            saveSharedPref(key, 0)
            allClear()
        }
        else {
            afterFocusChange(editText,key)
        }

    }

    private fun allClear() {
        if (binding.getUpPressure.text.isEmpty() && binding.getDownPressure.text.isEmpty() && binding.getPulse.text.isEmpty()) {
            forClear(binding.getUpPressure)
            forClear(binding.getDownPressure)
            forClear(binding.getPulse)
        }
    }

    private fun afterFocusChange(editText: EditText, key: String) {
        editText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && editText.text.isNotEmpty()) {
                afterKeyboardClosedOrLostFocusForHeart(editText, key)
            }
        }
    }

    private fun exit(editText: EditText) {

        if (editText.text.isEmpty()) {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_ic, 0)
        }

    }

    private fun forClear(editText: EditText) {

        if (editText.text.isEmpty()) {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    private fun saveHeartSettings(heart : String) {

        val editor = viewModel.settingsForHeart.edit()
        editor?.putString(Constants.PRESSURE, heart)?.apply()

    }

    private fun saveSharedPref(key : String, value : Int) {

        val editor = viewModel.settingsForHeart.edit()
        editor?.putInt(key, value)?.apply()

    }

    private fun afterKeyboardClosedOrLostFocusForHeart(editText: EditText, key: String) {

        if (editText.text.toString().toInt() in 30..220) {

            if (editText.text.toString().toInt() != viewModel.settingsForHeart.getInt(key, 0)) {

                if (binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty()) {
                    saveOrUpdateHeartBd()
                    viewModel.setCurrentPulse()
                    viewModel.setCurrentId()
                    viewModel.setCurrentDate()
                    viewModel.setCurrentLowerPressure()
                    viewModel.setCurrentUpperPressure()
                    saveHeartSettings(binding.ur1.text.toString() + "/" + binding.ur2.text.toString())

                }
            }

            //
            editText.setSelection(editText.text.toString().length)

            //
            saveSharedPref(key, editText.text.toString().toInt())

            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            binding.wentBack.isClickable = true
            binding.profile.isClickable = true

        } else {
            //
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_ic, 0)
            binding.wentBack.isClickable = false
            binding.profile.isClickable = false
        }

    }

    private fun forFocus(boolean: Boolean) {
        focusChange(binding.getUpPressure, boolean)
        focusChange(binding.getDownPressure, boolean)
        focusChange(binding.getPulse, boolean)
    }

    private fun focusChange(editText: EditText, boolean: Boolean) {
        editText.isCursorVisible = boolean
    }

    private fun saveOrUpdateHeartBd() {

        var date = 0L
        var id = 0
        val currentDate = Calendar.getInstance().timeInMillis

        viewModel.lastDate.observe(this@heartFragment.viewLifecycleOwner) {
            if (it != null) {
                date = it
            }
        }
        viewModel.lastId.observe(this@heartFragment.viewLifecycleOwner) {
            if (it != null) {
                id = it
            }
        }

        //Если новая дата не совпадает со старой -> insert
        if (SimpleDateFormat("dd.MM").format(currentDate) != SimpleDateFormat("dd.MM").format(date)) {

            val ourHeart = forHeartBd(currentDate, 0)
            viewModel.insertHeart(ourHeart)

        } else { //Если новая дата совпадает со старой -> update

            val ourHeart = forHeartBd(currentDate, id)
            viewModel.updateHeart(ourHeart)

        }

    }

    private fun forHeartBd(currentDate: Long, id: Int): Heart {
        return Heart(
            id = id,
            pressureUp = binding.getUpPressure.text.toString().toInt(),
            pressureDown = binding.getDownPressure.text.toString().toInt(),
            pulse = binding.getPulse.text.toString().toInt(),
            date = currentDate,
        )
    }

}