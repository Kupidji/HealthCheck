package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.model.medicines.service.MedicinesNotificationService
import com.example.healthcheck.util.Constants

class AddMedicinesViewModel(application: Application) : AndroidViewModel(application) {

    private var context = getApplication<Application>().applicationContext
    private var medicinesNotificationService = MedicinesNotificationService(context)

    fun createNotification(timeInMillis : Long, title : String, channelID : Int) {
        if (timeInMillis != 0L) {
            medicinesNotificationService.setRepetitiveAlarm(timeInMillis, title, channelID)
        }
    }

    suspend fun createMedicine(medicines: Medicines) {
        Repositories.medicinesRepository.createMedicine(medicines)
    }

}