package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentMedicinesBinding
import com.example.healthcheck.viewmodel.MedicinesRecyclerViewAdapter
import com.example.healthcheck.viewmodel.MedicinesViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class medicinesFragment : Fragment() {

    companion object {
        fun newInstance() = medicinesFragment()
    }

    private lateinit var viewModel: MedicinesViewModel
    private lateinit var binding : FragmentMedicinesBinding
    private lateinit var adapter : MedicinesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MedicinesViewModel::class.java)
        binding = FragmentMedicinesBinding.inflate(inflater)
        adapter = MedicinesRecyclerViewAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()
        val layoutManager = GridLayoutManager(this.context, 2)


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