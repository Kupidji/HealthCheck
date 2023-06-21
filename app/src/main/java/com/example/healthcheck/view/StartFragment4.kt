package com.example.healthcheck.view

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.databinding.FragmentStart4Binding
import com.example.healthcheck.viewmodel.StartFragment4ViewModel


class StartFragment4 : Fragment() {

    companion object {
        fun newInstance() = StartFragment4()
    }

    private lateinit var viewModel: StartFragment4ViewModel
    private lateinit var binding : FragmentStart4Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(StartFragment4ViewModel::class.java)
        binding = FragmentStart4Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup = binding.radioGender
        var gender = ""

        val navigation = findNavController()

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                -1 -> gender = ""
                binding.rbMale.id -> gender = "man"
                binding.rbFemale.id -> gender = "woman"
            }
        }

        binding.next.setOnClickListener {
            navigation.navigate(com.example.healthcheck.R.id.startFragment5)
        }
        
        // TODO: Use the ViewModel
    }

}