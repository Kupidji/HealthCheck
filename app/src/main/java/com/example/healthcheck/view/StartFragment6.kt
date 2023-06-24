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
import com.example.healthcheck.databinding.FragmentStart6Binding
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodel.StartFragment6ViewModel

class StartFragment6 : Fragment() {

    companion object {
        fun newInstance() = StartFragment6()
    }

    private lateinit var viewModel: StartFragment6ViewModel
    private lateinit var binding : FragmentStart6Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(StartFragment6ViewModel::class.java)
        binding = FragmentStart6Binding.inflate(inflater)
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

        val numberPicker2: NumberPicker = binding.numberPicker2
        numberPicker2.setMaxValue(300)
        numberPicker2.setMinValue(30)
        numberPicker2.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPicker2.setWrapSelectorWheel(false)

        binding.next.setOnClickListener {
            val editorforname = viewModel.settings.edit()
            editorforname?.putString(Constants.HEIGHT_START, binding.numberPicker2.value.toString())?.apply()
            var direction = StartFragment6Directions.actionStartFragment6ToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.next, navigation, direction, navOptions, navigate)
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })
    }

}