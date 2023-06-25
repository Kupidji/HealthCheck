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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.healthcheck.databinding.FragmentMedicinesBinding
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.model.medicines.viewmodel.MedicinesActionListener
import com.example.healthcheck.model.medicines.viewmodel.MedicinesRecyclerViewAdapter
import com.example.healthcheck.model.medicines.viewmodel.MedicinesViewModel

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

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()


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
                    medicines.totalMissed,
                )

                navigation.navigate(direction, navOptions)
            }

//            override fun onClickCheckBox(medicines: Medicines) {
//                //кинуть заполненость
//                //todo сделать on and off состояния
//                Toast.makeText(this@medicinesFragment.context, "Выполнено", Toast.LENGTH_SHORT).show()
//            }

        })

        binding.recyclerViewMedicines.layoutManager = layoutManager
        binding.recyclerViewMedicines.adapter = adapter
        viewModel.getAllMedicines().observe(this.viewLifecycleOwner) {
            adapter.medicinesList = it
        }

        if (checkCountOfMedicines()) binding.nothingThere.visibility = View.GONE
        else binding.nothingThere.visibility = View.VISIBLE

        binding.wentBack.setOnClickListener {
            //навигация и анимации
            val direction = medicinesFragmentDirections.actionMedicinesFragmentToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        binding.profile.setOnClickListener {
            //навигация и анимации
            val direction = medicinesFragmentDirections.actionMedicinesFragmentToProfileFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.profile, navigation, direction, navOptions, navigate)
        }

        binding.addNewMedicines.setOnClickListener {
            //навигация и анимации
            val direction = medicinesFragmentDirections.actionMedicinesFragmentToAddMedicinesFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.addNewMedicines, navigation, direction, navOptions, navigate)
        }

    }

    fun checkCountOfMedicines() : Boolean {
        return (adapter.medicinesList.isEmpty())
    }

}