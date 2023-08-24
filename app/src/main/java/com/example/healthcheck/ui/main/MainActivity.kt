package com.example.healthcheck.ui.main

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.data.FIRST_LAUNCH
import com.example.data.Repositories
import com.example.data.SETTINGS
import com.example.domain.AppDispatchers
import com.example.domain.usecase.settings.CheckCurrentDay
import com.example.domain.usecase.start.GetFirstLaunchCompleted
import com.google.android.material.R
import com.example.healthcheck.databinding.ActivityMainBinding
import com.example.healthcheck.util.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

////////██╗░░██╗███████╗░█████╗░██╗░░░░░████████╗██╗░░██╗////////░█████╗░██╗░░██╗███████╗░█████╗░██╗░░██╗
////////██║░░██║██╔════╝██╔══██╗██║░░░░░╚══██╔══╝██║░░██║////////██╔══██╗██║░░██║██╔════╝██╔══██╗██║░██╔╝
////////███████║█████╗░░███████║██║░░░░░░░░██║░░░███████║////////██║░░╚═╝███████║█████╗░░██║░░╚═╝█████═╝░
////////██╔══██║██╔══╝░░██╔══██║██║░░░░░░░░██║░░░██╔══██║////////██║░░██╗██╔══██║██╔══╝░░██║░░██╗██╔═██╗░
////////██║░░██║███████╗██║░░██║███████╗░░░██║░░░██║░░██║////////╚█████╔╝██║░░██║███████╗╚█████╔╝██║░╚██╗
////////╚═╝░░╚═╝╚══════╝╚═╝░░╚═╝╚══════╝░░░╚═╝░░░╚═╝░░╚═╝////////░╚════╝░╚═╝░░╚═╝╚══════╝░╚════╝░╚═╝░░╚═╝

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var hasNotificationPermissionGranted = false

    private val notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        hasNotificationPermissionGranted = isGranted
        if (!isGranted) {
            if (Build.VERSION.SDK_INT >= 33) {
                if (!shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) {
                    showSettingDialog()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //тема приложения
        val settingsTheme = applicationContext.getSharedPreferences(Constants.CHOOSEN_THEME, Context.MODE_PRIVATE)
        if (settingsTheme.contains(Constants.CHOOSEN_THEME)) {
            AppCompatDelegate.setDefaultNightMode(settingsTheme.getInt(Constants.CHOOSEN_THEME, AppCompatDelegate.MODE_NIGHT_YES))
        }

        //TODO убрать в некст обновлении
        val settings = this.applicationContext.getSharedPreferences(Constants.IS_FIRST_TIME, Context.MODE_PRIVATE)
        val settings2 = this.applicationContext.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit()
        val oldValue = settings.getBoolean(Constants.IS_FIRST_TIME, true)
        if (!oldValue) {
            settings2.putBoolean(FIRST_LAUNCH, oldValue).apply()
        }

        //Мониторит, который сегодня день, пока обнуляет время в экране сон
        lifecycleScope.launch(AppDispatchers.main) {
            val checkCurrentDay = CheckCurrentDay(
                settingsRepository = Repositories.settingsStorage,
                stepsRepository = Repositories.stepsRepository,
                sleepStorageRepository = Repositories.sleepStorage,
                weightRepository = Repositories.weightRepository,
            )
            checkCurrentDay.execute()
        }

        //Запрос на уведомления
        lifecycleScope.launch(AppDispatchers.main) {
            val navController = (binding.fragmentContainerView.getFragment<NavHostFragment>())
                .findNavController()

            val getFirstLaunchCompleted = GetFirstLaunchCompleted(repository = Repositories.settingsStorage)
            val status = getFirstLaunchCompleted.execute()

            if (settings.getBoolean(Constants.IS_FIRST_TIME, true) || status) {

                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(POST_NOTIFICATIONS)
                } else {
                    hasNotificationPermissionGranted = true
                }
                //Для восстановления уведомлений
                showSettingDialog2()
            }
            else {
                navController.graph.setStartDestination(com.example.healthcheck.R.id.mainFragment)
                navController.navigate(com.example.healthcheck.R.id.mainFragment)
            }
        }

    }

    //Функции для разрешения уведомлений
    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Material3)
            .setTitle("Разрешение на уведомления")
            .setMessage("Наше приложение использует уведомления для напоминания о приёме лекарств")
            .setPositiveButton("Ок") { _, _ ->
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showSettingDialog2() {
        MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Material3)
            .setTitle("Разрешение на автозапуск")
            .setMessage("Наше приложение использует уведомления для напоминания о приёме лекарств, но без автозапуска они не будут работать корректно, разрешите нашему приложению запускаться автоматически")
            .setPositiveButton("Разрешить") { _, _ ->
                try {
                    val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:" + this.getPackageName())
                    this.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                    this.startActivity(intent)
                }
            }
            .setNegativeButton("Запретить", null)
            .show()
    }

}

