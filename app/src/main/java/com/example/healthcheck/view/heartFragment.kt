package com.example.healthcheck.view

import android.graphics.Rect
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentHeartBinding
import com.example.healthcheck.model.heart.entities.Heart
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.model.heart.viewmodel.HeartViewModel
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

        var id = 0
        var date = 0L

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
            if (binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty()) {
                if (binding.getUpPressure.text.toString().toInt() in 30..220 && binding.getDownPressure.text.toString().toInt() in 30..220 && binding.getPulse.text.toString().toInt() in 30..220) {
                    saveOrUpdateHeartBd()
                }
            }
            if (binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty() &&
                binding.getUpPressure.text.toString().toInt() in 30..220 && binding.getDownPressure.text.toString().toInt() in 30..220 && binding.getPulse.text.toString().toInt() in 30..220) {
                saveHeartSettings(binding.getUpPressure.text.toString() + "/" + binding.getDownPressure.text.toString())
            }
           else {
                saveHeartSettings(binding.ur1.text.toString() + "/" + binding.ur2.text.toString())
            }

            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.mainFragment, true)
                .build()
            //навигация и анимации
            val direction = heartFragmentDirections.actionHeartFragmentToMainFragment()
            val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

//        afterFocusChange(binding.getUpPressure)
//        afterFocusChange(binding.getDownPressure)
//        afterFocusChange(binding.getPulse)

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            val heightDiff = view.rootView.height - r.height()

            //Если клавиатура убрана
            if (heightDiff < 0.2 * view.rootView.height) {

                //Скрыть фокус
                forFocus(false)

                //сохранение в бд, когда все 3 заполнены
                afterKeyboardIsDown(binding.getUpPressure, Constants.UPPER, id, date)
                afterKeyboardIsDown(binding.getDownPressure, Constants.LOWER, id, date)
                afterKeyboardIsDown(binding.getPulse, Constants.PULSE, id, date)


            } else {
                //Показать фокус
                forFocus(true)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty()) {
                    if (binding.getUpPressure.text.toString().toInt() in 30..220 && binding.getDownPressure.text.toString().toInt() in 30..220 && binding.getPulse.text.toString().toInt() in 30..220) {
                        saveOrUpdateHeartBd()
                    }
                }
                var navOptions = NavOptions.Builder()
                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                    .setPopUpTo(R.id.mainFragment, true)
                    .build()
                //навигация и анимации
                val direction = heartFragmentDirections.actionHeartFragmentToMainFragment()
                navigation.navigate(direction, navOptions)
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    //Клавиатура убрана
    private fun afterKeyboardIsDown(editText: EditText, key: String, id: Int, date : Long) {

        if (editText.text.isNotEmpty() && editText.isFocused) {
            //Сохранение
            afterKeyboardClosedOrLostFocusForHeart(editText, key, id, date)

        }

    }

    //После изменения фокуса
//    private fun afterFocusChange(editText: EditText) {
//        editText.setOnFocusChangeListener { v, hasFocus ->
//            if (!hasFocus && editText.text.isNotEmpty()) {
//                if (editText.text.toString().toInt() !in 30..220) {
//                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_ic, 0)
//                }
//                else {
//                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//                }
//            }
//        }
//    }

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
    private fun afterKeyboardClosedOrLostFocusForHeart(editText: EditText, key: String, id: Int, date: Long) {

        if (binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty()) {

        if (binding.getUpPressure.text.toString().toInt() in 30..220 && binding.getDownPressure.text.toString().toInt() in 30..220 && binding.getPulse.text.toString().toInt() in 30..220) {

            if (binding.getUpPressure.text.toString().toInt() != viewModel.settingsForHeart.getInt(Constants.UPPER, 0) ||
                binding.getDownPressure.text.toString().toInt() != viewModel.settingsForHeart.getInt(Constants.LOWER, 0) ||
                binding.getPulse.text.toString().toInt() != viewModel.settingsForHeart.getInt(Constants.PULSE, 0)) {

                if (binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty() &&
                    binding.getUpPressure.text.toString().toInt() in 30..220 && binding.getDownPressure.text.toString().toInt() in 30..220 && binding.getPulse.text.toString().toInt() in 30..220) {

                    //Сохранение в бд
                    saveOrUpdateHeartBdKeyBoard(id, date)

                    viewModel.setCurrentId()
                    viewModel.setCurrentDate()
                    viewModel.setCurrentLowerPressure()
                    viewModel.setCurrentUpperPressure()
                    viewModel.setCurrentPulse()

                    saveHeartSettings(binding.getUpPressure.text.toString() + "/" + binding.getDownPressure.text.toString())
                    saveSharedPref(Constants.PULSE, binding.getPulse.text.toString().toInt())
                    saveSharedPref(Constants.LOWER, binding.getDownPressure.text.toString().toInt())
                    saveSharedPref(Constants.UPPER, binding.getUpPressure.text.toString().toInt())
                }

            }

            editText.setSelection(editText.text.toString().length)

            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        } else {
            if (binding.getUpPressure.text.toString().toInt() !in 30..220) {
                binding.getUpPressure.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_ic, 0)
            }
            else {
                binding.getUpPressure.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
            if (binding.getDownPressure.text.toString().toInt() !in 30..220) {
                binding.getDownPressure.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_ic, 0)
            }
            else {
                binding.getDownPressure.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
            if (binding.getPulse.text.toString().toInt() !in 30..220) {
                binding.getPulse.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_ic, 0)
            }
            else {
                binding.getPulse.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0)
            }
        }
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

        var id = 0
        var date = 0L

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

        saveOrUpdateHeartBdKeyBoard(id, date)

    }

    private fun saveOrUpdateHeartBdKeyBoard(id: Int, date : Long) {

        val currentDate = Calendar.getInstance().timeInMillis
        Log.d("date", "current date: $currentDate day: $date id : $id ")

        //Если новая дата не совпадает со старой -> insert
        if (SimpleDateFormat("dd.MM").format(currentDate) != SimpleDateFormat("dd.MM").format(date)) {

            if (binding.getPulse.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getUpPressure.text.isNotEmpty()) {

                val ourHeart = forHeartBd(currentDate, 0)
                Log.d("update", "saveOrUpdateHeartBdKeyBoard: insert $ourHeart")
                viewModel.insertHeart(ourHeart)
            }

        } else { //Если новая дата совпадает со старой -> update

            if (binding.getPulse.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getUpPressure.text.isNotEmpty()) {

                val ourHeart = forHeartBd(currentDate, id)
                Log.d("update", "saveOrUpdateHeartBdKeyBoard: update $ourHeart")
                viewModel.updateHeart(ourHeart)

            }

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