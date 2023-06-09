package com.example.healthcheck

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.service.MedicinesNotificationService
import kotlinx.coroutines.launch

class MedicinesEditViewModel(application: Application) : AndroidViewModel(application) {

    private var context = getApplication<Application>().applicationContext
    private var service = MedicinesNotificationService(context)
    fun deleteMedicine(medicine : Medicines) {

        Toast.makeText(context, "Курс ${medicine.title} завершён", Toast.LENGTH_LONG).show()
        viewModelScope.launch {
            Repositories.medicinesRepository.deleteMedicine(medicine)
        }

    }

    fun deleteNotification(medicine : Medicines) {
        cancelNotification(medicine.timeOfNotify1, medicine.title, medicine.channelIDFirstTime)
        cancelNotification(medicine.timeOfNotify2, medicine.title, medicine.channelIDSecondTime)
        cancelNotification(medicine.timeOfNotify3, medicine.title, medicine.channelIDThirdTime)
        cancelNotification(medicine.timeOfNotify4, medicine.title, medicine.channelIDFourthTime)
    }

    private fun cancelNotification(timeInMills : Long, title : String, channelID : Int) {
        if (timeInMills != 0L) {
            service.cancelRepetitiveNotification(timeInMills, title, channelID)
        }
    }

}