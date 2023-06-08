package com.example.healthcheck

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class medicinesEditFragment : Fragment() {

    companion object {
        fun newInstance() = medicinesEditFragment()
    }

    private lateinit var viewModel: MedicinesEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medicines_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MedicinesEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}