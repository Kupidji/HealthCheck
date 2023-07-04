package com.example.healthcheck.view

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
import com.example.healthcheck.databinding.ActivityMainBinding
import com.example.healthcheck.util.Constants
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


////////██╗░░██╗███████╗░█████╗░██╗░░░░░████████╗██╗░░██╗////////░█████╗░██╗░░██╗███████╗░█████╗░██╗░░██╗
////////██║░░██║██╔════╝██╔══██╗██║░░░░░╚══██╔══╝██║░░██║////////██╔══██╗██║░░██║██╔════╝██╔══██╗██║░██╔╝
////////███████║█████╗░░███████║██║░░░░░░░░██║░░░███████║////////██║░░╚═╝███████║█████╗░░██║░░╚═╝█████═╝░
////////██╔══██║██╔══╝░░██╔══██║██║░░░░░░░░██║░░░██╔══██║////////██║░░██╗██╔══██║██╔══╝░░██║░░██╗██╔═██╗░
////////██║░░██║███████╗██║░░██║███████╗░░░██║░░░██║░░██║////////╚█████╔╝██║░░██║███████╗╚█████╔╝██║░╚██╗
////////╚═╝░░╚═╝╚══════╝╚═╝░░╚═╝╚══════╝░░░╚═╝░░░╚═╝░░╚═╝////////░╚════╝░╚═╝░░╚═╝╚══════╝░╚════╝░╚═╝░░╚═╝
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var hasNotificationPermissionGranted = false

    val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
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

        //Запрос на уведомления
        var settings = this.applicationContext.getSharedPreferences(Constants.IS_FIRST_TIME, Context.MODE_PRIVATE)
        if (settings.getBoolean(Constants.IS_FIRST_TIME, true)) {
            if (Build.VERSION.SDK_INT >= 33) {
                notificationPermissionLauncher.launch(POST_NOTIFICATIONS)
            } else {
                hasNotificationPermissionGranted = true
            }
            //Для восстановления уведомлений
            showSettingDialog2()
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
                    //Open the specific App Info page:
                    val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:" + this.getPackageName())
                    this.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    //Open the generic Apps page:
                    val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                    this.startActivity(intent)
                }
            }
            .setNegativeButton("Запретить", null)
            .show()
    }

}

