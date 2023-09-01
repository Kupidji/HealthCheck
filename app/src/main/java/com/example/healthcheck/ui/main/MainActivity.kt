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
import com.example.data.AGE
import com.example.data.CHOOSEN_THEME
import com.example.data.FIRST_LAUNCH
import com.example.data.FOREARM
import com.example.data.GENDER
import com.example.data.HEALTHY_EAT_VISIBILITY
import com.example.data.HEIGHT
import com.example.data.HIPS
import com.example.data.HIP_1
import com.example.data.HIP_2
import com.example.data.NAME
import com.example.data.NECK
import com.example.data.PROFILE
import com.example.data.Repositories
import com.example.data.SETTINGS
import com.example.data.SHIN
import com.example.data.STEPS
import com.example.data.STEPS_TARGET
import com.example.data.WAIST
import com.example.data.WEIGHT
import com.example.data.WRIST
import com.example.domain.AppDispatchers
import com.example.domain.usecase.start.GetFirstLaunchCompleted
import com.example.healthcheck.databinding.ActivityMainBinding
import com.example.healthcheck.notifications.service.MedicinesNotificationService
import com.example.healthcheck.notifications.service.NotificationService
import com.example.healthcheck.util.Constants
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar

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

        //Инициализирование дней в таблетках
        lifecycleScope.launch(AppDispatchers.io) {
            val settings = applicationContext.getSharedPreferences(Constants.LAST_UPDATE_DATE, Context.MODE_PRIVATE)
            val lastTimeFromSettings = settings.getLong(Constants.LAST_UPDATE_DATE, Calendar.getInstance().timeInMillis)
            val lastTime = SimpleDateFormat("dd.MM").format(lastTimeFromSettings)
            val calendar = Calendar.getInstance().timeInMillis
            val today = SimpleDateFormat("dd.MM").format(calendar)
            Log.d("test", "onCreate: ${lastTime} и ${today}")
            if (lastTime != today) {
                val list = Repositories.medicinesRepository.getAllMedicineList()
                for (medicine in list) {
                    medicine.currentDayOfCourse = getDiffDaysBetweenActions(curTime = calendar, actionTime = medicine.dateStart)
                    Log.d("test", "onCreate: ${medicine.currentDayOfCourse}")
                    Repositories.medicinesRepository.updateMedicine(medicine)
                }
            }

            //занесение текущей даты в settings
            val editor = settings.edit()
            if (settings.getLong(Constants.LAST_UPDATE_DATE, 0L) != Calendar.getInstance().timeInMillis) {
                editor?.putLong(Constants.LAST_UPDATE_DATE, Calendar.getInstance().timeInMillis)
                Log.d("Settings", "App: значение ${settings.getLong(Constants.LAST_UPDATE_DATE, 0L)} было изменено на ${Calendar.getInstance().timeInMillis}")
                editor?.apply()
            }
        }

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

            /** кроме этого **/
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

            //измерения в весе
            val oldSettingsWeight = this.applicationContext.getSharedPreferences(Constants.WEIGHT, Context.MODE_PRIVATE)
            val newSettingsWeight = this.applicationContext.getSharedPreferences(WEIGHT, Context.MODE_PRIVATE).edit()
            val oldNeck = oldSettingsWeight.getFloat(Constants.NECK, 0F)
            if (oldNeck != 0F) {
                newSettingsWeight.putFloat(NECK, oldNeck).apply()
            }
            val oldWaist = oldSettingsWeight.getFloat(Constants.WAIST, 0F)
            if (oldWaist != 0F) {
                newSettingsWeight.putFloat(WAIST, oldWaist).apply()
            }
            val oldForearm = oldSettingsWeight.getFloat(Constants.FOREARM, 0F)
            if (oldForearm != 0F) {
                newSettingsWeight.putFloat(FOREARM, oldForearm).apply()
            }
            val oldWrist = oldSettingsWeight.getFloat(Constants.WRIST, 0F)
            if (oldWrist != 0F) {
                newSettingsWeight.putFloat(WRIST, oldWrist).apply()
            }
            val oldBothHips = oldSettingsWeight.getFloat(Constants.HIPS, 0F)
            if (oldBothHips != 0F) {
                newSettingsWeight.putFloat(HIPS, oldBothHips).apply()
            }
            val oldHip1 = oldSettingsWeight.getFloat(Constants.HIP_1, 0F)
            if (oldHip1 != 0F) {
                newSettingsWeight.putFloat(HIP_1, oldHip1).apply()
            }
            val oldHip2 = oldSettingsWeight.getFloat(Constants.HIP_2, 0F)
            if (oldHip2 != 0F) {
                newSettingsWeight.putFloat(HIP_2, oldHip2).apply()
            }
            val oldShin = oldSettingsWeight.getFloat(Constants.SHIN, 0F)
            if (oldShin != 0F) {
                newSettingsWeight.putFloat(SHIN, oldShin).apply()
            }

            //Сохранение цели в шагах
            val oldSettingsSteps = this.applicationContext.getSharedPreferences(Constants.STEPS, Context.MODE_PRIVATE)
            val newSettingsSteps = this.applicationContext.getSharedPreferences(STEPS, Context.MODE_PRIVATE).edit()

            val oldStepsTarget = oldSettingsSteps.getInt(Constants.TARGET, 10000)
            if (oldStepsTarget != 10000) {
                newSettingsSteps.putInt(STEPS_TARGET, oldStepsTarget).apply()
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

    private fun getDiffDaysBetweenActions(curTime : Long, actionTime : Long) : Int {
        return ((curTime - actionTime).toFloat()/86400000F).toBigDecimal().setScale(0, RoundingMode.UP).toInt()
    }


}

