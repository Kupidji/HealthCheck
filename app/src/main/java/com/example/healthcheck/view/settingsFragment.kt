package com.example.healthcheck.view

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentSettingsBinding
import com.example.healthcheck.util.Constants
import com.example.healthcheck.viewmodel.SettingsViewModel

class settingsFragment : Fragment() {

    companion object {
        fun newInstance() = settingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding : FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        binding.wentBack.setOnClickListener {
            navigation.navigate(R.id.mainFragment)
        }

        binding.profile.setOnClickListener {
            navigation.navigate(R.id.profileFragment)
        }

        binding.whiteThemeBtn.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            val settings = this@settingsFragment.requireContext().applicationContext.getSharedPreferences(Constants.CHOOSEN_THEME, Context.MODE_PRIVATE)
            val editor = settings.edit()
            if (settings.getInt(Constants.CHOOSEN_THEME, 0) != AppCompatDelegate.MODE_NIGHT_NO) {
                editor.putInt(Constants.CHOOSEN_THEME, AppCompatDelegate.MODE_NIGHT_NO)
                    .apply()
            }
        }

        binding.nightThemeBtn.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            val settings = this@settingsFragment.requireContext().applicationContext.getSharedPreferences(Constants.CHOOSEN_THEME, Context.MODE_PRIVATE)
            val editor = settings.edit()
            if (settings.getInt(Constants.CHOOSEN_THEME, 0) != AppCompatDelegate.MODE_NIGHT_YES) {
                editor.putInt(Constants.CHOOSEN_THEME, AppCompatDelegate.MODE_NIGHT_YES)
                    .apply()
            }
        }

    }

}