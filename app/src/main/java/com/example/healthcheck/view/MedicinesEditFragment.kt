package com.example.healthcheck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentMedicinesEditBinding
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.model.medicines.viewmodel.MedicinesEditViewModel
import java.text.SimpleDateFormat

class MedicinesEditFragment : Fragment() {

    companion object {
        fun newInstance() = MedicinesEditFragment()
    }

    private lateinit var viewModel: MedicinesEditViewModel
    private lateinit var binding : FragmentMedicinesEditBinding
    private val args : MedicinesEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MedicinesEditViewModel::class.java)
        binding = FragmentMedicinesEditBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        binding.title.text = args.title

        if (args.firstTimeNotification != 0L)
            binding.firstTimeText.text = SimpleDateFormat("HH:mm").format(args.firstTimeNotification)
        if (args.secondTimeNotification != 0L)
            binding.secondTimeText.text = SimpleDateFormat("HH:mm").format(args.secondTimeNotification)
        if (args.thirdTimeNotification != 0L)
            binding.thirdTimeText.text = SimpleDateFormat("HH:mm").format(args.thirdTimeNotification)
        if (args.fourthTimeNotification != 0L)
            binding.fourthTimeText.text = SimpleDateFormat("HH:mm").format(args.fourthTimeNotification)

        binding.dateOfStart.text = SimpleDateFormat("dd MMM").format(args.dateOfStart)
        binding.currentDay.text = args.currentDay.toString()

        binding.progressBarMedicines.max = args.totalDuractionOfCourse
        binding.progressBarMedicines.progress = args.currentDay

        if (args.totalDuractionOfCourse != 0)
            binding.totalDays.text = "из ${args.totalDuractionOfCourse}"

        //binding.totalMissedText.text = args.totalMissed.toString()

        binding.wentBack.setOnClickListener {
            //навигация и анимации
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.medicinesFragment, true)
                .build()
            val direction = MedicinesEditFragmentDirections.actionMedicinesEditFragmentToMedicinesFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        binding.finishCourseBtn.setOnClickListener {
            var medicine = Medicines(
                args.id,
                args.title,
                args.dateOfStart,
                args.totalDuractionOfCourse,
                args.totalDuractionOfCourse,
                args.firstTimeNotification,
                args.firstTimeChannelID,
                args.secondTimeNotification,
                args.secondTimeChannelID,
                args.thirdTimeNotification,
                args.thirdTimeChannelID,
                args.fourthTimeNotification,
                args.fourthTimeChannelID,
                args.totalMissed,
            )
            viewModel.deleteMedicine(medicine)
            viewModel.deleteNotification(medicine)

            //навигация и анимации
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.medicinesFragment, true)
                .build()
            val direction = MedicinesEditFragmentDirections.actionMedicinesEditFragmentToMedicinesFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.finishCourseBtn, navigation, direction, navOptions, navigate)
        }

        binding.editBtn.setOnClickListener {
            val direction = MedicinesEditFragmentDirections.actionMedicinesEditFragmentToEditMedicineFragment (
                args.id,
                args.title,
                args.firstTimeNotification,
                args.firstTimeChannelID,
                args.secondTimeNotification,
                args.secondTimeChannelID,
                args.thirdTimeNotification,
                args.thirdTimeChannelID,
                args.fourthTimeNotification,
                args.fourthTimeChannelID,
                args.dateOfStart,
                args.currentDay,
                args.totalDuractionOfCourse,
                args.totalMissed,
            )
            //анимация и навигация
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .build()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.editBtn, navigation, direction, navOptions, navigate)
        }

    }

}