package com.example.healthcheck.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Medicines
import com.example.healthcheck.notifications.service.MedicinesNotificationService
import kotlinx.coroutines.launch

class MedicinesEditViewModel(application: Application) : AndroidViewModel(application) {

    private var context = getApplication<Application>().applicationContext
    private var service = MedicinesNotificationService(context)
    fun deleteMedicine(medicine : Medicines) {

        Toast.makeText(context, "Курс ${medicine.title} завершён", Toast.LENGTH_LONG).show()
        viewModelScope.launch {
            com.example.data.Repositories.medicinesRepository.deleteMedicine(medicine)
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