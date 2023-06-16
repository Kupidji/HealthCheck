package com.example.healthcheck.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.model.medicines.service.MedicinesNotificationService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditMedicineViewModel(application: Application) : AndroidViewModel(application) {

    private var context = getApplication<Application>().applicationContext
    private var medicinesNotificationService = MedicinesNotificationService(context)

    fun updateMedicine(medicines: Medicines) {
        viewModelScope.launch {
            Repositories.medicinesRepository.updateMedicine(medicines)
        }
    }

    fun createNotification(timeInMillis : Long, title : String, channelID : Int) {
        if (timeInMillis != 0L) {
            medicinesNotificationService.setRepetitiveAlarm(timeInMillis, title, channelID)
        }
    }

    fun cancelNotification(timeInMillis : Long, title : String, channelID : Int) {
        if (timeInMillis != 0L) {
            medicinesNotificationService.cancelRepetitiveNotification(timeInMillis, title, channelID)
        }
    }

}