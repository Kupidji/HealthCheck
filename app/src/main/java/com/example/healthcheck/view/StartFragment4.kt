package com.example.healthcheck.view

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.databinding.FragmentStart4Binding
import com.example.healthcheck.util.Constants
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

        var gender = false

        val navigation = findNavController()

        if (binding.rbMale.isChecked) {
            gender = true
        }

        if (binding.rbFemale.isChecked) {
            gender = false
        }

        binding.next.setOnClickListener {

            if (binding.getName.text.isNotEmpty()){
                val editorforname = viewModel.settings.edit()
                editorforname?.putString(Constants.FIO, binding.getName.text.toString())?.apply()
                editorforname?.putBoolean(Constants.GENDER, gender)?.apply()
                navigation.navigate(com.example.healthcheck.R.id.startFragment5)
            }
            else{
                binding.getName.error = "Поле пустое"

            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })

    }

}