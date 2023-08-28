package com.example.healthcheck.ui.weight

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.domain.AppDispatchers
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentWeightBinding
import com.example.healthcheck.util.animations.DigitsAnimation.digitsFloatAnimation
import com.example.healthcheck.util.animations.ProgressBarAnimation.animateProgressBar
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.weight.WeightViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class weightFragment : Fragment() {

    companion object {
        fun newInstance() = weightFragment()
    }

    private lateinit var viewModel: WeightViewModel
    private lateinit var binding : FragmentWeightBinding
    private var _id = 0
    private var _date = 0L
    private var _totalWeightForDay = 0F

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

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        //максимум для progressBar по стандарту 120
        changeMaxOfProgressBarsWeight(120F)

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.id.collect { id ->
                _id = id
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.date.collect { date ->
                _date = date
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.totalWeightForDay.collect { weight ->
                if (weight != 0F) {
                    binding.getWeight.setText(String.format(Locale.US,"%.1f", weight))
                }
                else {
                    binding.getWeight.setText("")
                }
                _totalWeightForDay = weight
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.totalWeightForWeek.collect { weight ->
                digitsFloatAnimation(binding.weekDiagramText, weight)
                changeProgressBar(progressBar = binding.progressBarWeightWeek, weight = weight)
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.totalWeightForMonth.collect { weight ->
                digitsFloatAnimation(binding.monthDiagramText, weight)
                changeProgressBar(progressBar = binding.progressBarWeightMonth, weight = weight)
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.neck.collect { neck ->
                binding.getNeck.setText(String.format(Locale.US,"%.1f", neck))
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.waist.collect { waist ->
                binding.getWaist.setText(String.format(Locale.US,"%.1f", waist))
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.forearm.collect { foream ->
                binding.getForearm.setText(String.format(Locale.US,"%.1f", foream))
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.wrist.collect { wrist ->
                binding.getWrist.setText(String.format(Locale.US,"%.1f", wrist))
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.bothHips.collect { bothHips ->
                binding.getHips.setText(String.format(Locale.US,"%.1f", bothHips))
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.leftHip.collect { leftHip ->
                binding.getHip1.setText(String.format(Locale.US,"%.1f", leftHip))
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.rightHip.collect { rightHip ->
                binding.getHip2.setText(String.format(Locale.US,"%.1f", rightHip))
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.shin.collect { shin ->
                binding.getShin.setText(String.format(Locale.US,"%.1f", shin))
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.contentOfFat.collect { value ->
                digitsFloatAnimation(binding.percentMass, value, symbol = "%")
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.bodyMassIndex.collect { value ->
                digitsFloatAnimation(binding.massIndex, value)
            }
        }

        binding.getWeight.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.getWeight.text.isNotEmpty()) {
                forWeight()
            }
        }

        var keyboardStatus = false
        view.viewTreeObserver.addOnGlobalLayoutListener {
            var r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            var heightDiff = view.rootView.height - r.height()

            //Если клавиатура убрана
            if (heightDiff < 0.2 * view.rootView.height) {

                if (!keyboardStatus) {
                    keyboardStatus = true
                    focusChange(binding.getWeight, false)
                    focusChange(binding.getNeck, false)
                    focusChange(binding.getWaist, false)
                    focusChange(binding.getForearm, false)
                    focusChange(binding.getWrist, false)
                    focusChange(binding.getHips, false)
                    focusChange(binding.getHip1, false)
                    focusChange(binding.getHip2, false)
                    focusChange(binding.getShin, false)

                    if (binding.getWeight.text.isNotEmpty() && binding.getWeight.isFocused) {
                        //Для сохранения веса
                        forWeight()
                    }

                    //Для сохранения обхватов
                    if (binding.getNeck.text.isNotEmpty() && binding.getNeck.isFocused) {
                        viewModel.setNeck(binding.getNeck.text.toString().toFloat())
                    }
                    else if (binding.getNeck.isFocused) {
                        viewModel.setNeck(0F)
                    }

                    if (binding.getWaist.text.isNotEmpty() && binding.getWaist.isFocused) {
                        viewModel.setWaist(binding.getWaist.text.toString().toFloat())
                    }
                    else if (binding.getWaist.isFocused){
                        viewModel.setWaist(0F)
                    }

                    if (binding.getForearm.text.isNotEmpty() && binding.getForearm.isFocused) {
                        viewModel.setForearm(binding.getForearm.text.toString().toFloat())
                    }
                    else if (binding.getForearm.isFocused){
                        viewModel.setForearm(0F)
                    }

                    if (binding.getWrist.text.isNotEmpty() && binding.getWrist.isFocused) {
                        viewModel.setWrist(binding.getWrist.text.toString().toFloat())
                    }
                    else if (binding.getWrist.isFocused){
                        viewModel.setWrist(0F)
                    }

                    if (binding.getHips.text.isNotEmpty() && binding.getHips.isFocused) {
                        viewModel.setBothHips(binding.getHips.text.toString().toFloat())
                    }
                    else if (binding.getHips.isFocused){
                        viewModel.setBothHips(0F)
                    }

                    if (binding.getHip1.text.isNotEmpty() && binding.getHip1.isFocused) {
                        viewModel.setLeftHip(binding.getHip1.text.toString().toFloat())
                    }
                    else if (binding.getHip1.isFocused){
                        viewModel.setLeftHip(0F)
                    }

                    if (binding.getHip2.text.isNotEmpty() && binding.getHip2.isFocused) {
                        viewModel.setRightHip(binding.getHip2.text.toString().toFloat())
                    }
                    else if (binding.getHip2.isFocused){
                        viewModel.setRightHip(0F)
                    }

                    if (binding.getShin.text.isNotEmpty() && binding.getShin.isFocused) {
                        viewModel.setShin(binding.getShin.text.toString().toFloat())
                    }
                    else if (binding.getShin.isFocused){
                        viewModel.setShin(0F)
                    }

                }

            }
            else {
                keyboardStatus = false
                focusChange(binding.getWeight, true)
                focusChange(binding.getNeck, true)
                focusChange(binding.getWaist, true)
                focusChange(binding.getForearm, true)
                focusChange(binding.getWrist, true)
                focusChange(binding.getHips, true)
                focusChange(binding.getHip1, true)
                focusChange(binding.getHip2, true)
                focusChange(binding.getShin, true)
            }

        }

        binding.wentBack.setOnClickListener {
            //навигация и анимации
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.mainFragment, true)
                .build()
            val direction = weightFragmentDirections.actionWeightFragmentToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        binding.historyBtn.setOnClickListener {
            //навигация и анимации
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.weightHistory, true)
                .build()
            val direction = weightFragmentDirections.actionWeightFragmentToWeightHistory()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.historyBtn, navigation, direction, navOptions, navigate)
        }

    }

    private fun focusChange(editText: EditText, boolean: Boolean) {
        editText.isCursorVisible = boolean
    }

    private fun forWeight() {
        if (binding.getWeight.text.toString().toFloat() in 1.0..635.0) {
            if (binding.getWeight.text.toString().toFloat() != _totalWeightForDay) {
                //Сохраняет или обновляет базу данных
                saveOrUpdateWeightBdKeyBoard()
            }
            binding.getWeight.setSelection(binding.getWeight.text.toString().length)
            binding.getWeight.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
        else {
            binding.getWeight.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.error_ic, 0)
        }

    }

    private fun saveOrUpdateWeightBdKeyBoard() {
        val currentDate = Calendar.getInstance().timeInMillis
        Log.d("date", "current date: $currentDate day: $_date id : $_id ")
        //Если новая дата не совпадает со старой -> insert
        if (SimpleDateFormat("dd.MM").format(currentDate) != SimpleDateFormat("dd.MM").format(_date)) {
            if (binding.getWeight.text.isNotEmpty()) {
                viewModel.insertWeight(weight = binding.getWeight.text.toString().toFloat())
            }
        }
        else { //Если новая дата совпадает со старой -> update
            if (binding.getWeight.text.isNotEmpty()) {
                viewModel.updateWeight(id = _id, weight = binding.getWeight.text.toString().toFloat())
            }
        }

    }

    private fun changeMaxOfProgressBarsWeight(target : Float) {
        if (target != 0F) {
            binding.progressBarWeightWeek.max = target.toInt()
            binding.progressBarWeightMonth.max = target.toInt()
        }
        else {
            binding.progressBarWeightWeek.max = 120
            binding.progressBarWeightMonth.max = 120
        }

    }

    private fun changeProgressBar(progressBar: ProgressBar, weight : Float) {
        if (weight != null) {
            animateProgressBar(progressBar, weight.toInt())
        }
    }

}