package com.example.healthcheck.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.models.Medicines
import com.example.domain.usecase.medicines.UpdateMedicine
import com.example.healthcheck.notifications.service.MedicinesNotificationService
import kotlinx.coroutines.launch

class EditMedicineViewModel(application: Application) : AndroidViewModel(application) {

    private var context = getApplication<Application>().applicationContext
    private var medicinesNotificationService = MedicinesNotificationService(context)

    fun updateMedicine(medicine : Medicines) {
        viewModelScope.launch {
            val updateMedicine = UpdateMedicine(repository = Repositories.medicinesRepository)
            updateMedicine.execute(medicine = medicine)
        }
    }

    fun updateNotifications(timeInMillis : Long, title : String, channelID : Int) {
        medicinesNotificationService.cancelRepetitiveNotification(timeInMillis = timeInMillis, message = title, channelID = channelID)
        if (timeInMillis != 0L) {
            medicinesNotificationService.setRepetitiveAlarm(timeInMillis = timeInMillis, message = title, channelID = channelID)
        }

    }

}