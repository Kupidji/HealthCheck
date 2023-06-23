package com.example.healthcheck.view

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentWeightBinding
import com.example.healthcheck.model.weight.entities.Weight
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodel.WeightViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class weightFragment : Fragment() {

    companion object {
        fun newInstance() = weightFragment()
    }

    private lateinit var viewModel: WeightViewModel
    private lateinit var binding : FragmentWeightBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeightBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(WeightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        var currentDate = Calendar.getInstance().timeInMillis

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        changeProgressBar()

        viewModel.totalWeightForDay.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.getWeight.setText(String.format(Locale.US,"%.1f", it))
            }
        }

        viewModel.day.observe(this@weightFragment.viewLifecycleOwner) {
            if ((SimpleDateFormat("dd").format(it)) != (SimpleDateFormat("dd").format(currentDate))) {
                binding.getWeight.setText("")
                saveDataForWeight(0F, Constants.WEIGHT_FOR_DAY)
                viewModel.setCurrentWeightForDay(0F)
            }
        }

        binding.progressBarWeightWeek.max = 120
        binding.progressBarWeightMonth.max = 120

        binding.wentBack.setOnClickListener {
            //навигация и анимации
            val direction = weightFragmentDirections.actionWeightFragmentToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        binding.profile.setOnClickListener {
            //навигация и анимации
            val direction = weightFragmentDirections.actionWeightFragmentToProfileFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.profile, navigation, direction, navOptions, navigate)
        }

        viewModel.totalWeightForMonth.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != null) {
                binding.averageWeightForMonth.setText(String.format(Locale.US,"%.1f", it))
            }
        }

        viewModel.neck.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.neckneck.setText(String.format(Locale.US,"%.1f", it))
            }
        }
        viewModel.waist.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.waistwaist.setText(String.format(Locale.US,"%.1f", it))
            }
        }
        viewModel.forearm.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.forearmforearm.setText(String.format(Locale.US,"%.1f", it))
            }
        }
        viewModel.wrist.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.wristwrist.setText(String.format(Locale.US,"%.1f", it))
            }
        }
        viewModel.hips.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.hipships.setText(String.format(Locale.US,"%.1f", it))
            }
        }
        viewModel.hip1.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.hiphip1.setText(String.format(Locale.US,"%.1f", it))
            }
        }
        viewModel.hip2.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.hiphip2.setText(String.format(Locale.US,"%.1f", it))
            }
        }
        viewModel.shin.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.shinshin.setText(String.format(Locale.US,"%.1f", it))
            }
        }

        //Фокус прешел на другой edittext
        binding.getWeight.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && binding.getWeight.text.isNotEmpty()) {
                forWeight()
            }
        }
        afterFocusChangeForMeasure(binding.neckneck, Constants.NECK, viewModel.neck)
        afterFocusChangeForMeasure(binding.waistwaist, Constants.WAIST,viewModel.waist)
        afterFocusChangeForMeasure(binding.forearmforearm, Constants.FOREARM,viewModel.forearm)
        afterFocusChangeForMeasure(binding.wristwrist, Constants.WRIST,viewModel.wrist)
        afterFocusChangeForMeasure(binding.hipships, Constants.HIPS, viewModel.hips)
        afterFocusChangeForMeasure(binding.hiphip1, Constants.HIP_1, viewModel.hip1)
        afterFocusChangeForMeasure(binding.hiphip2, Constants.HIP_2, viewModel.hip2)
        afterFocusChangeForMeasure(binding.shinshin, Constants.SHIN, viewModel.shin)

        view.viewTreeObserver.addOnGlobalLayoutListener {
            var r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            var heightDiff = view.rootView.height - r.height()

            //Если клавиатура убрана
            if (heightDiff < 0.2 * view.rootView.height) {

                binding.getWeight.isCursorVisible = false
                binding.neckneck.isCursorVisible = false
                binding.waistwaist.isCursorVisible = false
                binding.forearmforearm.isCursorVisible = false
                binding.wristwrist.isCursorVisible = false
                binding.hipships.isCursorVisible = false
                binding.hiphip1.isCursorVisible = false
                binding.hiphip2.isCursorVisible = false
                binding.shinshin.isCursorVisible = false

                if (binding.getWeight.text.isNotEmpty() && binding.getWeight.isFocused) {

                    //Для сохранения веса
                    forWeight()

                }

                //Для сохранения обхватов
                if (binding.neckneck.text.isNotEmpty() && binding.neckneck.isFocused) {
                    forMeasure(binding.neckneck, Constants.NECK, viewModel.neck)
                }
                if (binding.waistwaist.text.isNotEmpty() && binding.waistwaist.isFocused) {
                    forMeasure(binding.waistwaist, Constants.WAIST,viewModel.waist)
                }
                if (binding.forearmforearm.text.isNotEmpty() && binding.forearmforearm.isFocused) {
                    forMeasure(binding.forearmforearm, Constants.FOREARM,viewModel.forearm)
                }
                if (binding.wristwrist.text.isNotEmpty() && binding.wristwrist.isFocused) {
                    forMeasure(binding.wristwrist, Constants.WRIST,viewModel.wrist)
                }
                if (binding.hipships.text.isNotEmpty() && binding.hipships.isFocused) {
                    forMeasure(binding.hipships, Constants.HIPS, viewModel.hips)
                }
                if (binding.hiphip1.text.isNotEmpty() && binding.hiphip1.isFocused) {
                    forMeasure(binding.hiphip1, Constants.HIP_1, viewModel.hip1)
                }
                if (binding.hiphip2.text.isNotEmpty() && binding.hiphip2.isFocused) {
                    forMeasure(binding.hiphip2, Constants.HIP_2, viewModel.hip2)
                }
                if (binding.shinshin.text.isNotEmpty() && binding.shinshin.isFocused) {
                    forMeasure(binding.shinshin, Constants.SHIN, viewModel.shin)
                }

            }
            else{
                binding.getWeight.isCursorVisible = true
                binding.neckneck.isCursorVisible = true
                binding.waistwaist.isCursorVisible = true
                binding.forearmforearm.isCursorVisible = true
                binding.wristwrist.isCursorVisible = true
                binding.hipships.isCursorVisible = true
                binding.hiphip1.isCursorVisible = true
                binding.hiphip2.isCursorVisible = true
                binding.shinshin.isCursorVisible = true
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.totalWeightForDay.value?.let { saveDataForWeight(it, Constants.WEIGHT_FOR_DAY) }
    }

    private fun saveDataForWeight(measure : Float, constant : String) {

        val editor = viewModel.settingsWeight.edit()
        editor?.putFloat(
            constant,
            measure
        )?.apply()

    }

    private fun afterFocusChangeForMeasure(editText: EditText, constant: String, humanPart: MutableLiveData<Float?>) {

        editText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && editText.text.isNotEmpty()) {
                forMeasure(editText, constant, humanPart)
            }
        }

    }

    private fun forWeight() {

        if (binding.getWeight.text.toString().toFloat() in 1.0..635.0) {

            if (binding.getWeight.text.toString().toFloat() != viewModel.settingsWeight.getFloat(Constants.WEIGHT_FOR_DAY, 0F)) {

                var weight = binding.getWeight.text.toString().toFloat()

                //Сохраняет вес в SharedPref
                saveDataForWeight(binding.getWeight.text.toString().toFloat(), Constants.WEIGHT_FOR_DAY)

                //Обновляет вес в viewModel
                viewModel.setCurrentWeightForDay(binding.getWeight.text.toString().toFloat())

                //Сохраняет или обновляет базу данных
                saveOrUpdateWeightBd(weight)

                //Обновляет вес за неделю и за месяц и id date последней записи, так как обновилась база данных
                viewModel.setCurrentWeightForWeek()
                viewModel.setCurrentWeightForMonth()
                viewModel.setCurrentDateWeight()
                viewModel.setCurrentIdWeight()

                binding.getWeight.setSelection(binding.getWeight.text.toString().length)

            }
            else {
                saveDataForWeight(binding.getWeight.text.toString().toFloat(), Constants.WEIGHT_FOR_DAY)
                viewModel.setCurrentWeightForDay(binding.getWeight.text.toString().toFloat())
                binding.getWeight.setSelection(binding.getWeight.text.toString().length)
            }

            binding.getWeight.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        }
        else {
            binding.getWeight.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.error_ic, 0)
        }

    }

    private fun forMeasure(editText: EditText, constant : String, humanPart : MutableLiveData<Float?>) {

        if (editText.text.toString().toFloat() in 1.0..365.0) {
            saveDataForWeight(editText.text.toString().toFloat(), constant)
            viewModel.changeMeasure(humanPart, constant)
            editText.setSelection(editText.text.toString().length)
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0)
        }
        else {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.error_ic, 0)
        }

    }

    private fun saveOrUpdateWeightBd(weight : Float) {

        var day = 0L
        var id = 0
        var currentDate = Date().time

        viewModel.day.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != null) {
                day = it
            }
        }

        viewModel.id.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != null) {
                id = it
            }
        }

        //Если новая дата не совпадает со старой -> insert
        if (SimpleDateFormat("dd.MM").format(currentDate) != SimpleDateFormat("dd.MM").format(day)) {

            if (binding.getWeight.text.isNotEmpty()) {

                var ourWeight = forWeightBd(currentDate, 0, weight)

                viewModel.insertWeight(ourWeight)

            }

        } else { //Если новая дата совпадает со старой -> update

            if (binding.getWeight.text.isNotEmpty()) {

                var ourWeight = forWeightBd(currentDate, id, weight)

                viewModel.updateWeight(ourWeight)

            }

        }

    }

    //Заполняет поле для обнвление или вставки в базу данных
    private fun forWeightBd(currentDate : Long, id : Int, weight: Float) : Weight {

        var ourWeight = Weight(
            id = id,
            weight = weight,
            date = currentDate,
        )

        return ourWeight
    }

    private fun changeProgressBar() {

        viewModel.totalWeightForWeek.observe(this@weightFragment.viewLifecycleOwner) {
            binding.weekDiagramText.setText(String.format(Locale.US,"%.1f", it))
            if (it != null) {
                binding.progressBarWeightWeek.progress = it.toInt()
            }
        }
        viewModel.totalWeightForMonth.observe(this@weightFragment.viewLifecycleOwner) {
            binding.monthDiagramText.setText(String.format(Locale.US,"%.1f", it))
            if (it != null) {
                binding.progressBarWeightMonth.progress = it.toInt()
            }
        }

    }

}