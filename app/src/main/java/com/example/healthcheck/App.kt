package com.example.healthcheck

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.usecase.settings.GetApplicationTheme
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        val scope = CoroutineScope(AppDispatchers.io + Job())
        scope.launch {
            val getApplicationTheme = GetApplicationTheme(repository = Repositories.settingsStorage)
            AppCompatDelegate.setDefaultNightMode(getApplicationTheme.execute())
        }

        Log.d("Database", "База данных инициализирована")
        Repositories.init(applicationContext)

        //создание канала для уведомления
        createNotificationChannel()

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                Constants.MEDICINES_CHANNEL,
                Constants.MEDICINE,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Напоминание о приёме лекарственных препаратов"

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            val channel2 = NotificationChannel(
                Constants.REGULAR_CHANNEL,
                Constants.REGULAR,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel2.description = "Напоминание о вводе данных за день"

            val manager2 = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager2.createNotificationChannel(channel2)

        }
    }

}

