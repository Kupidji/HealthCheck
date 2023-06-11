package com.example.healthcheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.healthcheck.databinding.FragmentMedicinesEditBinding
import com.example.healthcheck.model.medicines.entities.Medicines
import java.text.SimpleDateFormat

class medicinesEditFragment : Fragment() {

    companion object {
        fun newInstance() = medicinesEditFragment()
    }

    private lateinit var viewModel: MedicinesEditViewModel
    private lateinit var binding : FragmentMedicinesEditBinding
    private val args : medicinesEditFragmentArgs by navArgs()

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

        binding.medicineTitle.text = args.title

        if (args.firstTimeNotification != 0L)
            binding.firstTimeText.text = SimpleDateFormat("HH:mm").format(args.firstTimeNotification)
        if (args.secondTimeNotification != 0L)
            binding.secondTimeText.text = SimpleDateFormat("HH:mm").format(args.secondTimeNotification)
        if (args.thirdTimeNotification != 0L)
            binding.thirdTimeText.text = SimpleDateFormat("HH:mm").format(args.thirdTimeNotification)
        if (args.fourthTimeNotification != 0L)
            binding.fourthTimeText.text = SimpleDateFormat("HH:mm").format(args.fourthTimeNotification)

        binding.dateStart.text = args.dateOfStart
        binding.currentDay.text = args.currentDay.toString()

        if (args.totalDuractionOfCourse != 0)
            binding.totalDays.text = "из ${args.totalDuractionOfCourse}"

        binding.wentBack.setOnClickListener {
            navigation.navigate(R.id.medicinesFragment)
        }

        binding.finishCourse.setOnClickListener {
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
            )
            viewModel.deleteMedicine(medicine)
            viewModel.deleteNotification(medicine)
            navigation.navigate(R.id.medicinesFragment)
        }

    }

}