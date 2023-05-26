package com.example.healthcheck

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class mainFragment1 : Fragment() {

    companion object {
        fun newInstance() = mainFragment1()
    }

    private lateinit var viewModel: MainFragment1ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_fragment1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainFragment1ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}