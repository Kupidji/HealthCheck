package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
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

        var gender = viewModel.settings.getBoolean(Constants.GENDER, true)

        binding.nameText.setText(viewModel.settings.getString(Constants.FIO, ""))
        binding.ageText.setText(viewModel.settings.getInt(Constants.AGE, 0).toString())
        binding.heightText.setText(viewModel.settings.getFloat(Constants.HEIGHT_START, 0F).toString())
        if (gender) {
            binding.rbMale.toggle()
        }
        else {
            binding.rbFemale.toggle()
        }

        binding.radioGender.setOnCheckedChangeListener { group, checkedId ->
            gender = binding.rbMale.isChecked
        }

        binding.wentBack.setOnClickListener {
            //подумать над навигацией
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
                val editorForProfile = viewModel.settings.edit()
                if (binding.nameText.text.toString() != viewModel.settings.getString(Constants.FIO, "")) {
                    editorForProfile?.putString(Constants.FIO, binding.nameText.text.toString())
                        ?.apply()
                }
                if (binding.ageText.text.toString() != viewModel.settings.getInt(Constants.AGE, 0).toString()) {
                    editorForProfile?.putInt(Constants.AGE, binding.ageText.text.toString().toInt())
                        ?.apply()
                }
                if (binding.heightText.text.toString() != viewModel.settings.getFloat(Constants.HEIGHT_START, 0F).toString()) {
                    editorForProfile?.putFloat(Constants.HEIGHT_START, binding.heightText.text.toString().toFloat())?.apply()
                }
                editorForProfile?.putBoolean(Constants.GENDER, gender)?.apply()
                //todo посылать информацию в saveInstanceStateSettings
                //навигация анимации
                var navOptions = NavOptions.Builder()
                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                    .setPopUpTo(R.id.mainFragment, true)
                    .build()

                val direction = profileFragmentDirections.actionProfileFragmentToMainFragment()
                val navigate =
                    { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
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