package com.example.healthcheck.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentStart6Binding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.start.StartFragment6ViewModel

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

        val navigation = findNavController()

        val numberPicker2: NumberPicker = binding.numberPicker2
        numberPicker2.maxValue = 300
        numberPicker2.minValue = 100
        numberPicker2.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPicker2.wrapSelectorWheel = false
        binding.heightText.setText(numberPicker2.value.toString())

        numberPicker2.setOnValueChangedListener { picker, oldVal, newVal ->
            binding.heightText.setText(newVal.toString())
        }

        binding.next.setOnClickListener {
            if (binding.heightText.text.toString().isNotEmpty()) {
                if (binding.heightText.text.toString().toFloat() in 100.0..300.0) {
                    viewModel.setHeight(binding.heightText.text.toString().toFloat())
                    binding.textInputLayout3.error = null

                    //навигация и анимации
                    var navOptions = NavOptions.Builder()
                        .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                        .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                        .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                        .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                        .setPopUpTo(R.id.mainFragment, true)
                        .build()
                    var direction = StartFragment6Directions.actionStartFragment6ToStartFragment7()
                    val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
                    buttonChangeScreenAnimation(binding.next, navigation, direction, navOptions, navigate)
                }
                else {
                    binding.textInputLayout3.error = this.requireContext().getString(R.string.wrongValue)
                }

            }
            else {
                binding.textInputLayout3.error = this.requireContext().getString(R.string.mandatoryValue)
            }

        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })
    }

}