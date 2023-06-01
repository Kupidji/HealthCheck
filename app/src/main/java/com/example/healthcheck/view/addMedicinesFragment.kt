package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentAddMedicinesBinding
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.viewmodel.AddMedicinesViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class addMedicinesFragment : Fragment() {

    companion object {
        fun newInstance() = addMedicinesFragment()
    }

    private lateinit var viewModel: AddMedicinesViewModel
    private lateinit var binding : FragmentAddMedicinesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AddMedicinesViewModel::class.java)
        binding = FragmentAddMedicinesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        binding.wentBack.setOnClickListener {
            navigation.navigate(R.id.medicinesFragment)
        }

        binding.profile.setOnClickListener {
            navigation.navigate(R.id.profileFragment)
        }

        binding.saveMedicine.setOnClickListener {
            if (binding.getTitle.text != null) {

                lifecycleScope.launch {
                    var currentDate = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(Date())

                    var durationOfCourse : Int
                    if (binding.getCountOfDays.text.isNotEmpty()) {
                        durationOfCourse = binding.getCountOfDays.text as Int
                    }
                    else {
                        durationOfCourse = 0
                    }

                    val medicines = Medicines (
                        title = binding.getTitle.text.toString(),
                        dateStart = currentDate,
                        durationOfCourse = durationOfCourse,
                        currentDayOfCourse = 1,
                        timeOfNotify1 = 0,
                        timeOfNotify2 = 0,
                        timeOfNotify3 = 0,
                        timeOfNotify4 = 0,
                        //TODO доделать timePicker
                    )
                    viewModel.createMedicine(medicines)
                    navigation.navigate(R.id.medicinesFragment)
                }

            }
            else {
                //TODO доделать message
                binding.getTitle.error
            }

        }

    }

}