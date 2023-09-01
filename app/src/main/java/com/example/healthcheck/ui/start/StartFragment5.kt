package com.example.healthcheck.ui.start

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
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentStart5Binding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.start.StartFragment5ViewModel


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
        numberPicker0.maxValue = 99
        numberPicker0.minValue = 10
        numberPicker0.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPicker0.wrapSelectorWheel = false
        binding.ageText.setText(numberPicker0.value.toString())

        numberPicker0.setOnValueChangedListener { picker, oldVal, newVal ->
            binding.ageText.setText(newVal.toString())
        }

        binding.next.setOnClickListener {
            if (binding.ageText.text.toString().isNotEmpty()) {
                if (binding.ageText.text.toString().toInt() in 10..100) {
                    binding.textInputLayout2.error = null
                    viewModel.setAge(binding.ageText.text.toString().toInt())

                    val direction = StartFragment5Directions.actionStartFragment5ToStartFragment6()
                    val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
                    buttonChangeScreenAnimation(binding.next, navigation, direction, navOptions, navigate)
                }
                else {
                    binding.textInputLayout2.error = this.requireContext().getString(R.string.wrongValue)
                }

            }
            else {
                binding.textInputLayout2.error = this.requireContext().getString(R.string.mandatoryValue)
            }

        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })

    }

}