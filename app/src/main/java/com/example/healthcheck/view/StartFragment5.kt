package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthcheck.R
import com.example.healthcheck.viewmodel.StartFragment5ViewModel

class StartFragment5 : Fragment() {

    companion object {
        fun newInstance() = StartFragment5()
    }

    private lateinit var viewModel: StartFragment5ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start_5, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StartFragment5ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}