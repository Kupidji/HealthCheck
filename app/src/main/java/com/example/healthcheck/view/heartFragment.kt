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
            //Для главного экрана
            saveHeartSettings(binding.ur1.text.toString() + "/" + binding.ur2.text.toString())
            //навигация и анимации
            val direction = heartFragmentDirections.actionHeartFragmentToMainFragment()
            val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        //навигация и анимации
        binding.profile.setOnClickListener {
            //для главного экрана
            saveHeartSettings(binding.ur1.text.toString() + "/" + binding.ur2.text.toString())
            //навигация и анимации
            val direction = heartFragmentDirections.actionHeartFragmentToProfileFragment()
            val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
            buttonChangeScreenAnimation(binding.profile, navigation, direction, navOptions, navigate)
        }

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            val heightDiff = view.rootView.height - r.height()

            //Если клавиатура убрана
            if (heightDiff < 0.2 * view.rootView.height) {

                //Скрыть фокус
                forFocus(false)

                //сохранение в бд, когда все 3 заполнены
                afterKeyboardIsDown(binding.getUpPressure, Constants.UPPER)
                afterKeyboardIsDown(binding.getDownPressure, Constants.LOWER)
                afterKeyboardIsDown(binding.getPulse, Constants.PULSE)

            } else {
                //Показать фокус
                forFocus(true)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Для глвного экрана
        saveHeartSettings(binding.ur1.text.toString() + "/" + binding.ur2.text.toString())
    }

    //Клавиатура убрана
    private fun afterKeyboardIsDown(editText: EditText, key: String, ) {

        if (editText.text.isNotEmpty() && editText.isFocused) {
            //Сохранение
            afterKeyboardClosedOrLostFocusForHeart(editText, key)

        }
        else if (editText.text.isEmpty() && editText.isFocused){
            saveSharedPref(key, 0)
        }
        else {
            //После изменения фокуса
            afterFocusChange(editText,key)
        }

    }

    //После изменения фокуса
    private fun afterFocusChange(editText: EditText, key: String) {
        editText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && editText.text.isNotEmpty()) {
                afterKeyboardClosedOrLostFocusForHeart(editText, key)
            }
        }
    }

    //Сохранение для главного экрана
    private fun saveHeartSettings(heart : String) {

        val editor = viewModel.settingsForHeart.edit()
        editor?.putString(Constants.PRESSURE, heart)?.apply()

    }

    //Сохранение для sharedpref
    private fun saveSharedPref(key : String, value : Int) {

        val editor = viewModel.settingsForHeart.edit()
        editor?.putInt(key, value)?.apply()

    }

    //Клавиатура закрыта ил потерян фокус
    private fun afterKeyboardClosedOrLostFocusForHeart(editText: EditText, key: String) {

        if (editText.text.toString().toInt() in 30..220) {

            if (editText.text.toString().toInt() != viewModel.settingsForHeart.getInt(key, 0)) {

                //Если все 3 не пусты и значения в соответвющих пределах, то сохранение в бд
                if (binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty() &&
                    binding.getUpPressure.text.toString().toInt() in 30..220 && binding.getDownPressure.text.toString().toInt() in 30..220 && binding.getPulse.text.toString().toInt() in 30..220) {

                    //Сохранение в бд
                    saveOrUpdateHeartBd()

                    //Изменения показателей
                    viewModel.setCurrentPulse()
                    viewModel.setCurrentId()
                    viewModel.setCurrentDate()
                    viewModel.setCurrentLowerPressure()
                    viewModel.setCurrentUpperPressure()

                    //Для главного экрана
                    saveHeartSettings(binding.ur1.text.toString() + "/" + binding.ur2.text.toString())

                }
            }

            editText.setSelection(editText.text.toString().length)

            //Сохранение соответствующего поля
            saveSharedPref(key, editText.text.toString().toInt())

            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        } else {
            //Ошибка
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_ic, 0)
        }

    }

    //Дляя фокуса
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