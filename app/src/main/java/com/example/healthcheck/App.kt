package com.example.healthcheck

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.util.Constants

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        //инициализация бд
        Repositories.init(applicationContext)

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                Constants.MEDICINES_CHANNEL,
                Constants.MEDICINE,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Напоминание о приеме лекарственного препарата"

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

}