package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.databinding.FragmentStart5Binding
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodel.StartFragment5ViewModel

class StartFragment5 : Fragment() {

    companion object {
        fun newInstance() = StartFragment5()
    }

    private lateinit var viewModel: StartFragment5ViewModel
    private lateinit var binding : FragmentStart5Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(StartFragment5ViewModel::class.java)
        binding = FragmentStart5Binding.inflate(inflater)
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

        val navigation = findNavController()

        val numberPicker0: NumberPicker = binding.numberPicker0
        numberPicker0.setMaxValue(99)
        numberPicker0.setMinValue(1)
        numberPicker0.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPicker0.setWrapSelectorWheel(false)

        val numberPicker1: NumberPicker = binding.numberPicker1
        numberPicker1.setMaxValue(200)
        numberPicker1.setMinValue(5)
        numberPicker1.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPicker1.setWrapSelectorWheel(false)

        binding.next.setOnClickListener {
            val editorforname = viewModel.settings.edit()
            editorforname?.putString(Constants.AGE, binding.numberPicker0.value.toString())?.apply()
            editorforname?.putString(Constants.WEIGHT_START, binding.numberPicker1.value.toString())?.apply()
            val direction = StartFragment5Directions.actionStartFragment5ToStartFragment6()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.next, navigation, direction, navOptions, navigate)
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })

    }

}