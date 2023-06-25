package com.example.healthcheck.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.databinding.FragmentSettingsBinding
import com.example.healthcheck.model.settings.viewmodel.SettingsViewModel
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation

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
        val settings = this@settingsFragment.requireContext().applicationContext.getSharedPreferences(Constants.HEALTHY_EAT_VISIBILITY, Context.MODE_PRIVATE)

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        binding.wentBack.setOnClickListener {
            //навигация и анимации
            val direction = settingsFragmentDirections.actionSettingsFragmentToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        binding.profile.setOnClickListener {
            //навигация и анимации
            val direction = settingsFragmentDirections.actionSettingsFragmentToProfileFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.profile, navigation, direction, navOptions, navigate)
        }

        binding.switchHealtyEat.isChecked =
            settings.getBoolean(Constants.HEALTHY_EAT_VISIBILITY, true)

        binding.switchHealtyEat.setOnCheckedChangeListener { buttonView, isChecked ->
            val editor = settings.edit()
            if (isChecked) {
                editor.putBoolean(Constants.HEALTHY_EAT_VISIBILITY, true)
                    .apply()
            }
            else {
                editor.putBoolean(Constants.HEALTHY_EAT_VISIBILITY, false)
                    .apply()
            }
        }

        binding.whiteThemeBtn.setOnClickListener {
            binding.whiteThemeBtn.animate()
                .setDuration(75)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .withEndAction {
                    binding.whiteThemeBtn.animate()
                        .setDuration(75)
                        .scaleX(1f)
                        .scaleY(1f)
                        .withEndAction {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            val settings = this@settingsFragment.requireContext().applicationContext.getSharedPreferences(Constants.CHOOSEN_THEME, Context.MODE_PRIVATE)
                            val editor = settings.edit()
                            if (settings.getInt(Constants.CHOOSEN_THEME, 0) != AppCompatDelegate.MODE_NIGHT_NO) {
                                editor.putInt(Constants.CHOOSEN_THEME, AppCompatDelegate.MODE_NIGHT_NO)
                                    .apply()
                            }
                        }
                }
        }

        binding.nightThemeBtn.setOnClickListener {
            binding.nightThemeBtn.animate()
                .setDuration(75)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .withEndAction {
                    binding.nightThemeBtn.animate()
                        .setDuration(75)
                        .scaleX(1f)
                        .scaleY(1f)
                        .withEndAction {
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

        binding.mobileBtn.setOnClickListener {
            binding.mobileBtn.animate()
                .setDuration(75)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .withEndAction {
                    binding.mobileBtn.animate()
                        .setDuration(75)
                        .scaleX(1f)
                        .scaleY(1f)
                        .withEndAction {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            val settings = this@settingsFragment.requireContext().applicationContext.getSharedPreferences(Constants.CHOOSEN_THEME, Context.MODE_PRIVATE)
                            val editor = settings.edit()
                            if (settings.getInt(Constants.CHOOSEN_THEME, 0) != AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
                                editor.putInt(Constants.CHOOSEN_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                                    .apply()
                            }
                        }
                }
        }

        binding.notificationProblemText.setOnClickListener {
            openAppNotificationSettings(this.requireContext())
        }

    }

    private fun openAppNotificationSettings(context: Context) {
        val intent = Intent().apply {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                }
                else -> {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    addCategory(Intent.CATEGORY_DEFAULT)
                    data = Uri.parse("package:" + context.packageName)
                }
            }
        }

        context.startActivity(intent)
    }

}