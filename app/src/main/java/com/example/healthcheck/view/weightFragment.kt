package com.example.healthcheck.view

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.log10

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
        var weight = 0F
        var height = 0F
        var neck = 0F
        var waist = 0F
        var forearm = 0F
        var wrist = 0F
        var hips = 0F
        var hip1 = 0F
        var shin = 0F
        var gender = true
        var age = 0


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

        viewModel.heightStart.observe(this@weightFragment.viewLifecycleOwner) {
            binding.heightInWeight.setText(String.format(Locale.US,"%.1f", it))
            if (it != null) {
                height = it
            }
        }

        viewModel.weightStart.observe(this@weightFragment.viewLifecycleOwner) {
            binding.averageWeightForMonth.setText(String.format(Locale.US,"%.1f", it))
            if (it != null) {
                binding.massIndex.setText(String.format(
                    Locale.US,
                    "%.1f",
                    imt(it.toFloat(),binding.heightInWeight.text.toString().toFloat()))
                )
            }
        }

        viewModel.gender.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != null) {
                gender = it
            }
        }

        viewModel.age.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != null) {
                age = it
            }
        }

        viewModel.neck.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.neckneck.setText(String.format(Locale.US,"%.1f", it))
            }
            if (it != null) {
                neck = it
            }
        }
        viewModel.waist.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.waistwaist.setText(String.format(Locale.US,"%.1f", it))
            }
            if (it != null) {
                waist = it
            }
        }
        viewModel.forearm.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.forearmforearm.setText(String.format(Locale.US,"%.1f", it))
            }
            if (it != null) {
                forearm = it
            }
        }
        viewModel.wrist.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.wristwrist.setText(String.format(Locale.US,"%.1f", it))
            }
            if (it != null) {
                wrist = it
            }
        }
        viewModel.hips.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.hipships.setText(String.format(Locale.US,"%.1f", it))
            }
            if (it != null) {
                hips = it
            }
        }
        viewModel.hip1.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.hiphip1.setText(String.format(Locale.US,"%.1f", it))
            }
            if (it != null) {
                hip1 = it
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
            if (it != null) {
                shin = it
            }
        }

        viewModel.fat.observe(this@weightFragment.viewLifecycleOwner) {
            if (
                binding.neckneck.text.isNotEmpty() &&
                binding.waistwaist.text.isNotEmpty() &&
                binding.forearmforearm.text.isNotEmpty() &&
                binding.wristwrist.text.isNotEmpty() &&
                binding.hipships.text.isNotEmpty() &&
                binding.hiphip1.text.isNotEmpty() &&
                binding.hiphip2.text.isNotEmpty() &&
                binding.shinshin.text.isNotEmpty() &&
                binding.percentMass.text.toString() != it.toString()
            ) {
                binding.percentMass.setText(
                    String.format(
                        Locale.US, "%.1f",
                        fatTotal(
                            viewModel.gender.value.toString().toBoolean(),
                            viewModel.age.value.toString().toInt(),
                            viewModel.weightStart.value.toString().toFloat(),
                            viewModel.heightStart.value.toString().toFloat(),
                            binding.waistwaist.text.toString().toFloat(),
                            binding.wristwrist.text.toString().toFloat(),
                            binding.hipships.text.toString().toFloat(),
                            binding.hiphip1.text.toString().toFloat(),
                            binding.forearmforearm.text.toString().toFloat(),
                            binding.shinshin.text.toString().toFloat(),
                            binding.neckneck.text.toString().toFloat()
                        )
                    )
                )
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

                forFocus(false)

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
                forFocus(true)
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.totalWeightForDay.value?.let { saveDataForWeight(it, Constants.WEIGHT_FOR_DAY) }
    }

    private fun focusChange(editText: EditText, boolean: Boolean) {
        editText.isCursorVisible = boolean
    }

    private fun forFocus(boolean: Boolean) {
        focusChange(binding.getWeight, boolean)
        focusChange(binding.neckneck, boolean)
        focusChange(binding.waistwaist, boolean)
        focusChange(binding.forearmforearm, boolean)
        focusChange(binding.wristwrist, boolean)
        focusChange(binding.hipships, boolean)
        focusChange(binding.hiphip1, boolean)
        focusChange(binding.hiphip2, boolean)
        focusChange(binding.shinshin, boolean)
    }

    private fun saveDataForWeight(measure : Float, constant : String) {

        val editor = viewModel.settingsWeight.edit()
        editor?.putFloat(
            constant,
            measure
        )?.apply()

    }

    private fun afterFocusChangeForMeasure(editText: EditText, constant: String, humanPart: MutableLiveData<Float>) {
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

    private fun forMeasure(editText: EditText, constant : String, humanPart : MutableLiveData<Float>) {

        if (editText.text.toString().toFloat() in 1.0..365.0) {
            saveDataForWeight(editText.text.toString().toFloat(), constant)
            viewModel.changeMeasure(humanPart, constant)
            editText.setSelection(editText.text.toString().length)
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0)
            if (
                binding.neckneck.text.isNotEmpty() &&
                binding.waistwaist.text.isNotEmpty() &&
                binding.forearmforearm.text.isNotEmpty() &&
                binding.wristwrist.text.isNotEmpty() &&
                binding.hipships.text.isNotEmpty() &&
                binding.hiphip1.text.isNotEmpty() &&
                binding.hiphip2.text.isNotEmpty() &&
                binding.shinshin.text.isNotEmpty()
            ) {
                saveFat(binding.percentMass.text.toString().toFloat())
                viewModel.changeFat(binding.percentMass.text.toString().toFloat())
                if (binding.percentMass.text.toString() != viewModel.settingsWeight.getFloat(Constants.FAT, 0F).toString()) {
                    binding.percentMass.setText(
                        String.format(
                            Locale.US, "%.1f",
                            fatTotal(
                                viewModel.gender.value.toString().toBoolean(),
                                viewModel.age.value.toString().toInt(),
                                viewModel.weightStart.value.toString().toFloat(),
                                viewModel.heightStart.value.toString().toFloat(),
                                binding.neckneck.text.toString().toFloat(),
                                binding.wristwrist.text.toString().toFloat(),
                                binding.hipships.text.toString().toFloat(),
                                binding.hiphip1.text.toString().toFloat(),
                                binding.forearmforearm.text.toString().toFloat(),
                                binding.shinshin.text.toString().toFloat(),
                                binding.neckneck.text.toString().toFloat()
                            )
                        )
                    )
                }
            }

        }
        else {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.error_ic, 0)
        }

    }

    private fun saveFat(res : Float) {

        var editorForFat = viewModel.settingsWeight.edit()
        editorForFat?.putFloat(Constants.FAT, res)?.apply()

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

    private fun imt(Mass: Float, Height: Float): Float {
        Log.d("-inf", "Imt: ${10000 * Mass / (Height * Height)} ")
        Log.d("iii", "${Mass}")
        return 10000 * Mass / (Height * Height)
    }

   private fun fatImt(gender : Boolean, age : Int, mass : Float, height : Float) : Float {
       Log.d("-inf", "fatImt: ${(1.2 * imt(mass, height) + (0.23 * age) - (10.8 * 1) - 5.4).toFloat()} ")
       return if (gender) {
           (1.2 * imt(mass, height) + (0.23 * age) - (10.8 * 1) - 5.4).toFloat()
       } else {
           (1.2 * imt(mass, height) + (0.23 * age) - (10.8 * 0) - 5.4).toFloat()
       }
    }
    private fun fatYMSA(gender: Boolean, weight: Float, waist : Float) : Float {
        Log.d("-inf", "fatYMSA: ${((-98.42+(4.15*waist/2.54)-(0.082*(weight/0.454)))/(weight/0.454)*100).toFloat()} ")
        return if (gender) {
            ((-98.42+(4.15*waist/2.54)-(0.082*(weight/0.454)))/(weight/0.454)*100).toFloat()
        } else {
            ((-76.76+(4.15*waist/2.54)-(0.082*(weight/0.454)))/(weight/0.454)*100).toFloat()
        }
    }

    private fun fatUSA(waist: Float, hips: Float, wrist: Float, hip1 : Float, gender: Boolean, forearm: Float, neck: Float, height: Float) : Float {
        Log.d("-inf", "fatUSA: ${(495/(1.0324-0.19077* log10(waist-neck)+0.15456* log10(height)) - 450).toFloat()}")
        return if (!gender) {
            (495/(1.29579-0.35004* log10(waist+hips-neck)+0.221* log10(height)) - 450).toFloat()
        } else {
            (495/(1.0324-0.19077* log10(waist-neck)+0.15456* log10(height)) - 450).toFloat()
        }
    }

    private fun fatTotal(gender : Boolean, age : Int, weight: Float, height: Float, waist: Float, wrist: Float, hips: Float, hip1: Float, forearm: Float, shin: Float, neck: Float) : Float {
        Log.d("-inf", "fatTotal: ${(fatImt(gender, age, weight, height) + fatYMSA(gender, weight, waist) + fatUSA(waist, hips, wrist, hip1, gender, forearm, neck, height)) / 3} ")
        return (fatImt(gender, age, weight, height) + fatYMSA(gender, weight, waist) + fatUSA(waist, hips, wrist, hip1, gender, forearm, neck, height)) / 3
    }

}