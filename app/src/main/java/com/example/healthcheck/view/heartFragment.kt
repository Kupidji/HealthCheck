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
import com.example.healthcheck.databinding.FragmentHeartBinding
import com.example.healthcheck.viewmodel.HeartViewModel

class heartFragment : Fragment() {

    companion object {
        fun newInstance() = heartFragment()
    }

    private lateinit var viewModel: HeartViewModel
    private lateinit var binding : FragmentHeartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HeartViewModel::class.java)
        binding = FragmentHeartBinding.inflate(inflater)
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
            val direction = heartFragmentDirections.actionHeartFragmentToMainFragment()
            navigation.navigate(direction, navOptions)
        }

        binding.profile.setOnClickListener {
            val direction = heartFragmentDirections.actionHeartFragmentToProfileFragment()
            navigation.navigate(direction, navOptions)
        }

    }

}