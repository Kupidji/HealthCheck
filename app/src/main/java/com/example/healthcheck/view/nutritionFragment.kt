package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.databinding.FragmentNutritionBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.model.nutrition.viewmodel.NutritionViewModel

class nutritionFragment : Fragment() {

    companion object {
        fun newInstance() = nutritionFragment()
    }

    private lateinit var viewModel: NutritionViewModel
    private lateinit var binding : FragmentNutritionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNutritionBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(NutritionViewModel::class.java)
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
            //навигация и анимации
            val direction = nutritionFragmentDirections.actionNutritionFragmentToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        binding.profile.setOnClickListener {
            //навигация и анимации
            val direction = nutritionFragmentDirections.actionNutritionFragmentToProfileFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.profile, navigation, direction, navOptions, navigate)
        }

    }

}