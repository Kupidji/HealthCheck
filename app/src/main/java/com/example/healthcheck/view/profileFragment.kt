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
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentProfileBinding
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodel.ProfileViewModel

class profileFragment : Fragment() {

    companion object {
        fun newInstance() = profileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
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

        binding.nameText.setText(viewModel.settings.getString(Constants.FIO, ""))
        binding.ageText.setText(viewModel.settings.getString(Constants.AGE, ""))
        binding.weightText.setText(viewModel.settings.getString(Constants.WEIGHT_START, ""))
        binding.heightText.setText(viewModel.settings.getString(Constants.HEIGHT_START, ""))

        binding.wentBack.setOnClickListener {
            //подумать над навигацией
            val direction = profileFragmentDirections.actionProfileFragmentToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        binding.confirm.setOnClickListener {
            val editorforname = viewModel.settings.edit()
            if (binding.nameText.text.toString() != viewModel.settings.getString(Constants.FIO, "")){
                editorforname?.putString(Constants.FIO, binding.nameText.text.toString())?.apply()
            }
            if (binding.ageText.text.toString() != viewModel.settings.getString(Constants.AGE, "")){
                editorforname?.putString(Constants.AGE, binding.ageText.text.toString())?.apply()
            }
            if (binding.heightText.text.toString() != viewModel.settings.getString(Constants.HEIGHT_START, "")){
                editorforname?.putString(Constants.HEIGHT_START, binding.heightText.text.toString())?.apply()
            }
            if (binding.weightText.text.toString() != viewModel.settings.getString(Constants.WEIGHT_START, "")){
                editorforname?.putString(Constants.WEIGHT_START, binding.weightText.text.toString())?.apply()
            }
            //todo посылать информацию в saveInstanceStateSettings
            //навигация анимации
            val direction = profileFragmentDirections.actionProfileFragmentToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.confirm, navigation, direction, navOptions, navigate)
        }

    }

}