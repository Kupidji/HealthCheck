package com.example.healthcheck

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        //тема приложения
        val settingsTheme = applicationContext.getSharedPreferences(Constants.CHOOSEN_THEME, Context.MODE_PRIVATE)
        if (settingsTheme.contains(Constants.CHOOSEN_THEME)) {
            AppCompatDelegate.setDefaultNightMode(settingsTheme.getInt(Constants.CHOOSEN_THEME, AppCompatDelegate.MODE_NIGHT_YES))
        }

        //инициализация бд
        Log.d("Database", "База данных инициализирована")
        Repositories.init(applicationContext)

        //создание канала для уведомления
        createNotificationChannel()

        //инкрементация прогресса курса в medicines каждый день
        val settings = applicationContext.getSharedPreferences(Constants.LAST_UPDATE_DATE, Context.MODE_PRIVATE)

        val lastTime = SimpleDateFormat("dd").format(settings.getLong(Constants.LAST_UPDATE_DATE, Calendar.getInstance().timeInMillis))
        val today = SimpleDateFormat("dd").format(Calendar.getInstance().timeInMillis)
        Log.d("Settings", "App: сравнение дат ${lastTime} и ${today}")
        if (lastTime != today) {
            val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
            appScope.launch {
                val list = Repositories.medicinesRepository.getAllMedicineList()
                for (medicine in list) {
                    medicine.currentDayOfCourse++
                    Repositories.medicinesRepository.updateMedicine(medicine)
                }
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
            channel.description = "Напоминание о вводе данных за день"

            val manager2 = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager2.createNotificationChannel(channel2)

        }
    }

}

