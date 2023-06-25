package com.example.healthcheck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.databinding.FragmentStart4Binding
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
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

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

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

                val direction = StartFragment4Directions.actionStartFragment4ToStartFragment5()
                val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
                buttonChangeScreenAnimation(binding.next, navigation, direction, navOptions, navigate)
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