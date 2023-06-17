package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentWeightBinding
import com.example.healthcheck.viewmodel.WeightViewModel

class weightFragment : Fragment() {

    companion object {
        fun newInstance() = weightFragment()
    }

    private lateinit var viewModel: WeightViewModel
    private lateinit var binding : FragmentWeightBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeightBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(WeightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        binding.wentBack.setOnClickListener {
            val direction = weightFragmentDirections.actionWeightFragmentToMainFragment()
            navigation.navigate(direction, navOptions)
        }

        binding.profile.setOnClickListener {
            val direction = weightFragmentDirections.actionWeightFragmentToProfileFragment()
            navigation.navigate(direction, navOptions)
        }


    }

}