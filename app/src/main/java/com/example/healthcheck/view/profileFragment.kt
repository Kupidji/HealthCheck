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
import com.example.healthcheck.databinding.FragmentProfileBinding
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

        binding.wentBack.setOnClickListener {
            //подумать над навигацией
            val direction = profileFragmentDirections.actionProfileFragmentToMainFragment()

            //анимация
            binding.wentBack.animate()
                .setDuration(25)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .withEndAction {
                    //навигация
                    navigation.navigate(direction, navOptions)
                }
        }

        binding.confirm.setOnClickListener {
            //todo посылать информацию в saveInstanceStateSettings
            val direction = profileFragmentDirections.actionProfileFragmentToMainFragment()

            //анимация
            binding.confirm.animate()
                .setDuration(25)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .withEndAction {
                    //навигация
                    navigation.navigate(direction, navOptions)
                }
        }

    }

}