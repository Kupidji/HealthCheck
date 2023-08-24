package com.example.healthcheck.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.domain.AppDispatchers
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentProfileBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch

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

        var _name = ""
        var _age = 0
        var _height = 0F
        var _gender = false

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.userName.collect { name ->
                binding.nameText.setText("$name")
                _name = name
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.age.collect { age ->
                binding.ageText.setText("$age")
                _age = age
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.height.collect { height ->
                binding.heightText.setText("$height")
                _height = height
            }
        }

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.gender.collect { gender ->
                _gender = gender

                if (gender) {
                    binding.rbMale.toggle()
                }
                else {
                    binding.rbFemale.toggle()
                }

            }
        }

        binding.radioGender.setOnCheckedChangeListener { _, _ ->
            _gender = binding.rbMale.isChecked
        }

        binding.wentBack.setOnClickListener {
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.mainFragment, true)
                .build()
            val direction = profileFragmentDirections.actionProfileFragmentToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        binding.confirm.setOnClickListener {
            if (binding.nameText.text.isNotEmpty() && binding.ageText.text.isNotEmpty() && binding.heightText.text.isNotEmpty()) {
                if (binding.nameText.text.toString() != _name) {
                    viewModel.setUserName(name = binding.nameText.text.toString())
                }

                if (binding.ageText.text.toString().toInt() != _age) {
                    viewModel.setAge(age = binding.ageText.text.toString().toInt())
                }

                if (binding.heightText.text.toString().toFloat() != _height) {
                    viewModel.setHeight(height = binding.heightText.text.toString().toFloat())
                }

                viewModel.setGender(gender = _gender)

                //навигация анимации
                var navOptions = NavOptions.Builder()
                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                    .setPopUpTo(R.id.mainFragment, true)
                    .build()
                val direction = profileFragmentDirections.actionProfileFragmentToMainFragment()
                val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
                buttonChangeScreenAnimation(binding.confirm, navigation, direction, navOptions, navigate)
            }
            else {
                if(binding.nameText.text.isEmpty()) {
                    binding.nameText.error = "Обязательно поле"
                }

                if(binding.ageText.text.isEmpty()) {
                    binding.ageText.error = "Обязательно поле"
                }

                if(binding.heightText.text.isEmpty()) {
                    binding.heightText.error = "Обязательно поле"
                }
            }
        }

    }

}