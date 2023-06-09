package com.example.healthcheck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentMedicinesBinding
import com.example.healthcheck.medicinesEditFragment
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.viewmodel.MedicinesActionListener
import com.example.healthcheck.viewmodel.MedicinesRecyclerViewAdapter
import com.example.healthcheck.viewmodel.MedicinesViewModel

class medicinesFragment : Fragment() {

    companion object {
        fun newInstance() = medicinesFragment()
    }

    private lateinit var viewModel: MedicinesViewModel
    private lateinit var binding : FragmentMedicinesBinding
    private lateinit var adapter : MedicinesRecyclerViewAdapter
    private lateinit var navigation : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MedicinesViewModel::class.java)
        binding = FragmentMedicinesBinding.inflate(inflater)
        navigation = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(this.context, 2)

        adapter = MedicinesRecyclerViewAdapter(object : MedicinesActionListener {

            override fun onClickBox(medicines: Medicines) {
                val direction = medicinesFragmentDirections.actionMedicinesFragmentToMedicinesEditFragment (
                    medicines.title,
                    medicines.timeOfNotify1,
                    medicines.channelIDFirstTime,
                    medicines.timeOfNotify2,
                    medicines.channelIDSecondTime,
                    medicines.timeOfNotify3,
                    medicines.channelIDThirdTime,
                    medicines.timeOfNotify4,
                    medicines.channelIDFourthTime,
                    medicines.dateStart,
                    medicines.currentDayOfCourse,
                    medicines.durationOfCourse,
                    medicines.id,
                )
                navigation.navigate(direction)
            }

            override fun onClickCheckBox(medicines: Medicines) {
                //кинуть заполненость
                Toast.makeText(this@medicinesFragment.context, "Выполнено", Toast.LENGTH_SHORT).show()
            }

        })

        binding.recyclerViewMedicines.layoutManager = layoutManager
        binding.recyclerViewMedicines.adapter = adapter
        viewModel.getAllMedicines().observe(this.viewLifecycleOwner) {
            adapter.medicinesList = it
        }

        if (checkCountOfMedicines()) binding.nothingThere.visibility = View.GONE
        else binding.nothingThere.visibility = View.VISIBLE

        binding.wentBack.setOnClickListener {
            navigation.navigate(R.id.mainFragment)
        }

        binding.profile.setOnClickListener {
            navigation.navigate(R.id.profileFragment)
        }

        binding.addNewMedicines.setOnClickListener {
            navigation.navigate(R.id.addMedicinesFragment)
        }

    }

    fun checkCountOfMedicines() : Boolean {
        return (adapter.medicinesList.isEmpty())
    }

}