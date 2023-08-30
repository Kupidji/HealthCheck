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
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.data.AGE
import com.example.data.CHOOSEN_THEME
import com.example.data.FIRST_LAUNCH
import com.example.data.GENDER
import com.example.data.HEALTHY_EAT_VISIBILITY
import com.example.data.HEIGHT
import com.example.data.NAME
import com.example.data.PROFILE
import com.example.data.Repositories
import com.example.data.SETTINGS
import com.example.domain.AppDispatchers
import com.example.domain.usecase.settings.GetApplicationTheme
import com.example.domain.usecase.start.GetFirstLaunchCompleted
import com.example.healthcheck.databinding.ActivityMainBinding
import com.example.healthcheck.notifications.service.MedicinesNotificationService
import com.example.healthcheck.notifications.service.NotificationService
import com.example.healthcheck.util.Constants
import com.google.android.material.R
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
        lifecycleScope.launch(AppDispatchers.main) {
            val getApplicationTheme = GetApplicationTheme(repository = Repositories.settingsStorage)
            AppCompatDelegate.setDefaultNightMode(getApplicationTheme.execute())
        }

//        //тема приложения
//        val settingsTheme = applicationContext.getSharedPreferences(Constants.CHOOSEN_THEME, Context.MODE_PRIVATE)
//        if (settingsTheme.contains(Constants.CHOOSEN_THEME)) {
//            AppCompatDelegate.setDefaultNightMode(settingsTheme.getInt(Constants.CHOOSEN_THEME, AppCompatDelegate.MODE_NIGHT_YES))
//        }

        //TODO убрать в некст обновлении
        /*   отсюда    */
        val UPDATE = "1.3 UPDATE"
        //первый запуск
        val oldSettingsFirstTime = this.applicationContext.getSharedPreferences(Constants.IS_FIRST_TIME, Context.MODE_PRIVATE)
        val isUpdate = oldSettingsFirstTime.contains(Constants.IS_FIRST_TIME)

        val newSettingsFirstTime = this.applicationContext.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit()
        val oldValueFirstTime = oldSettingsFirstTime.getBoolean(Constants.IS_FIRST_TIME, true)
        if (!oldValueFirstTime) {
            newSettingsFirstTime.putBoolean(FIRST_LAUNCH, oldValueFirstTime).apply()
        }

        val updateCheck = this.applicationContext.getSharedPreferences(UPDATE,Context.MODE_PRIVATE)
        if (updateCheck.getBoolean(UPDATE, true) && isUpdate) {
            updateCheck.edit().putBoolean(UPDATE, false).apply()

            //восстановление уведомлений
            val medicinesNotificationService = MedicinesNotificationService(context = this.applicationContext)
            medicinesNotificationService.repairNotifications()
            val notificationService = NotificationService(context = this.applicationContext)
            notificationService.repairNotifications()

            //Профиль(имя, возраст, рост, пол)
            val oldSettingsStart = this.applicationContext.getSharedPreferences(Constants.START, Context.MODE_PRIVATE)
            val oldSettingsName = oldSettingsStart.getString(Constants.FIO, "")
            val oldSettingsAge = oldSettingsStart.getInt(Constants.AGE, 10)
            val oldSettingsHeight = oldSettingsStart.getFloat(Constants.HEIGHT_START, 100F)
            val oldSettingsGender = oldSettingsStart.getBoolean(Constants.GENDER, true)

            val newSettingsProfile = this.applicationContext.getSharedPreferences(PROFILE, Context.MODE_PRIVATE).edit()
            if (oldSettingsName != "") {
                newSettingsProfile.putString(NAME, oldSettingsName).apply()
            }
            if (oldSettingsAge != 10) {
                newSettingsProfile.putInt(AGE, oldSettingsAge).apply()
            }
            if (oldSettingsHeight != 100F) {
                newSettingsProfile.putFloat(HEIGHT, oldSettingsHeight).apply()
            }
            if (oldSettingsGender != true) {
                newSettingsProfile.putBoolean(GENDER, oldSettingsGender).apply()
            }

            //настройки(тема и видимость экрана с едой)
            val oldSettingsHealthyEat
                    = this.applicationContext.getSharedPreferences(Constants.HEALTHY_EAT_VISIBILITY, Context.MODE_PRIVATE).getBoolean(Constants.HEALTHY_EAT_VISIBILITY, true)
            val oldSettingsAppTheme
                    = this.applicationContext.getSharedPreferences(Constants.CHOOSEN_THEME, Context.MODE_PRIVATE).getInt(CHOOSEN_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            val newSettingsSettings = this.applicationContext.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit()
            if (oldSettingsHealthyEat != true) {
                newSettingsSettings.putBoolean(HEALTHY_EAT_VISIBILITY, oldSettingsHealthyEat).apply()
            }
            newSettingsSettings.putInt(CHOOSEN_THEME, oldSettingsAppTheme).apply()
            Log.d("Database", "onCreate: Инициализировал все старые константы")
            /*    до сюда     */
        }

        //Запрос на уведомления
        lifecycleScope.launch(AppDispatchers.main) {
            val getFirstLaunchCompleted = GetFirstLaunchCompleted(repository = Repositories.settingsStorage)
            val status = getFirstLaunchCompleted.execute()

            if (status) {

                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(POST_NOTIFICATIONS)
                } else {
                    hasNotificationPermissionGranted = true
                }

                //Для восстановления уведомлений
                showSettingDialog2()
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
                    intent.data = Uri.parse("package:" + this.packageName)
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

