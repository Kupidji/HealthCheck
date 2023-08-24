package com.example.healthcheck.ui

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.domain.AppDispatchers
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentSettingsBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.SettingsViewModel
import kotlinx.coroutines.launch

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

        lifecycleScope.launch(AppDispatchers.main) {
            viewModel.nutritionVisibility.collect { visibility ->
                binding.switchHealtyEat.isChecked = visibility
            }
        }

        binding.switchHealtyEat.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch(AppDispatchers.main) {
                viewModel.setNutritionVisibility(visibility = isChecked)
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
                            viewModel.setTheme(AppCompatDelegate.MODE_NIGHT_NO)
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
                            viewModel.setTheme(AppCompatDelegate.MODE_NIGHT_YES)
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
                            viewModel.setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        }
                }
        }

        binding.notificationProblemText.setOnClickListener {
            openAppNotificationSettings(this.requireContext())
        }

        binding.wentBack.setOnClickListener {
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.mainFragment, true)
                .build()
            //навигация и анимации
            val direction = settingsFragmentDirections.actionSettingsFragmentToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
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