package com.example.healthcheck.ui.start

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
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentStart4Binding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.start.StartFragment4ViewModel

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

        var gender = true

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        val navigation = findNavController()

        binding.radioGender.setOnCheckedChangeListener { _, _ ->
            gender = binding.rbMale.isChecked
        }

        binding.next.setOnClickListener {
            if (binding.getName.text.toString().isNotEmpty() && binding.radioGender.checkedRadioButtonId != -1) {
                viewModel.setName(binding.getName.text.toString())
                viewModel.setGender(gender)

                val direction = StartFragment4Directions.actionStartFragment4ToStartFragment5()
                val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
                buttonChangeScreenAnimation(binding.next, navigation, direction, navOptions, navigate)
            }
            else {
                binding.textInputLayout.error = this.requireContext().getString(R.string.emptyString)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })

    }

}