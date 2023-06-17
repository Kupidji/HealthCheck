package com.example.healthcheck.view

import android.graphics.Rect
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentWeightBinding
import com.example.healthcheck.viewmodel.WeightViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import com.example.healthcheck.model.weight.entities.Weight
import com.example.healthcheck.util.Constants

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

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        changeProgressBar()

        viewModel.totalWeightForDay.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.getWeight.setText(String.format("%.1f", it))
            }
        }

        binding.progressBarWeightWeek.max = 120
        binding.progressBarWeightMonth.max = 120

        binding.wentBack.setOnClickListener {
            val direction = weightFragmentDirections.actionWeightFragmentToMainFragment()
            navigation.navigate(direction, navOptions)
        }

        binding.profile.setOnClickListener {
            val direction = weightFragmentDirections.actionWeightFragmentToProfileFragment()
            navigation.navigate(direction, navOptions)
        }

        viewModel.totalWeightForMonth.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != null) {
                binding.averageWeightForMonth.setText(String.format("%.1f", it))
            }
        }

        viewModel.neck.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.neckneck.setText(String.format("%.1f", it))
            }
        }
        viewModel.waist.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.waistwaist.setText(String.format("%.1f", it))
            }
        }
        viewModel.forearm.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.forearmforearm.setText(String.format("%.1f", it))
            }
        }
        viewModel.wrist.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.wristwrist.setText(String.format("%.1f", it))
            }
        }
        viewModel.hips.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.hipships.setText(String.format("%.1f", it))
            }
        }
        viewModel.hip1.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.hiphip1.setText(String.format("%.1f", it))
            }
        }
        viewModel.hip2.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.hiphip2.setText(String.format("%.1f", it))
            }
        }
        viewModel.shin.observe(this@weightFragment.viewLifecycleOwner) {
            if (it != 0F) {
                binding.shinshin.setText(String.format("%.1f", it))
            }
        }

        view.viewTreeObserver.addOnGlobalLayoutListener {
            var r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            var heightDiff = view.rootView.height - r.height()

            //TODO Когда пишешь цель которая на кнопке, то первый раз стирает, второй раз нет
            //TODO Если при одной открытой клавиатуре записать и шаги и цель, то обновиться только то что последнее
            //Если клавиатура убрана
            if (heightDiff < 0.2 * view.rootView.height) {

                if (binding.getWeight.text.isNotEmpty() &&
                    binding.getWeight.text.toString().toFloat() != viewModel.settingsWeight.getFloat(
                        Constants.WEIGHT_FOR_DAY,0F
                    ) && binding.getWeight.isFocused) {

                    //Сохраняет вес в SharedPref
                    saveDataForWeight(binding.getWeight.text.toString().toFloat(), Constants.WEIGHT_FOR_DAY)

                    //Обновляет вес в viewModel
                    viewModel.setCurrentWeightForDay(binding.getWeight.text.toString().toFloat())

                    //Сохраняет или обновляет базу данных
                    saveOrUpdateWeightBd()

                    //Обновляет вес за неделю и за месяц и id date последней записи, так как обновилась база данных
                    viewModel.setCurrentWeightForWeek()
                    viewModel.setCurrentWeightForMonth()
                    viewModel.setCurrentDateWeight()
                    viewModel.setCurrentIdWeight()

                    binding.getWeight.clearFocus()

                }

                forMeasure(binding.neckneck, Constants.NECK)
                forMeasure(binding.waistwaist, Constants.WAIST)
                forMeasure(binding.forearmforearm, Constants.FOREARM)
                forMeasure(binding.wristwrist, Constants.WRIST)
                forMeasure(binding.hipships, Constants.HIPS)
                forMeasure(binding.hiphip1, Constants.HIP_1)
                forMeasure(binding.hiphip2, Constants.HIP_2)
                forMeasure(binding.shinshin, Constants.SHIN)

                viewModel.changeMeasure(viewModel.neck, Constants.NECK)
                viewModel.changeMeasure(viewModel.waist, Constants.WAIST)
                viewModel.changeMeasure(viewModel.forearm, Constants.FOREARM)
                viewModel.changeMeasure(viewModel.wrist, Constants.WRIST)
                viewModel.changeMeasure(viewModel.hips, Constants.HIPS)
                viewModel.changeMeasure(viewModel.hip1, Constants.HIP_1)
                viewModel.changeMeasure(viewModel.hip2, Constants.HIP_2)
                viewModel.changeMeasure(viewModel.shin, Constants.SHIN)

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

    private fun forMeasure(editText: EditText, constant : String) {

        if (editText.text.isNotEmpty() && editText.isFocused) {
            saveDataForWeight(editText.text.toString().toFloat(), constant)
            editText.clearFocus()
        }

    }

    private fun saveOrUpdateWeightBd() {

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

                var ourWeight = forWeightBd(currentDate, 0)

                viewModel.insertWeight(ourWeight)

            }

        } else { //Если новая дата совпадает со старой -> update

            if (binding.getWeight.text.isNotEmpty()) {

                var ourWeight = forWeightBd(currentDate, id)

                viewModel.updateWeight(ourWeight)

            }

        }

    }

    //Заполняет поле для обнвление или вставки в базу данных
    private fun forWeightBd(currentDate : Long, id : Int) : Weight {

        var ourWeight = Weight(
            id = id,
            weight = binding.getWeight.text.toString().toFloat(),
            date = currentDate,
        )

        saveDataForWeight(binding.getWeight.text.toString().toFloat(), Constants.WEIGHT_FOR_DAY)

        return ourWeight
    }

    private fun changeProgressBar() {

        viewModel.totalWeightForWeek.observe(this@weightFragment.viewLifecycleOwner) {
            binding.weekDiagramText.setText(String.format("%.1f", it))
            if (it != null) {
                binding.progressBarWeightWeek.progress = it.toInt()
            }
        }
        viewModel.totalWeightForMonth.observe(this@weightFragment.viewLifecycleOwner) {
            binding.monthDiagramText.setText(String.format("%.1f", it))
            if (it != null) {
                binding.progressBarWeightMonth.progress = it.toInt()
            }
        }


    }

}