package com.example.healthcheck.viewmodels.medicine

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.models.Medicines
import com.example.domain.usecase.medicines.InsertMedicine
import com.example.healthcheck.models.MedicineParams
import com.example.healthcheck.notifications.service.MedicinesNotificationService
import com.example.healthcheck.util.RandomUtil
import kotlinx.coroutines.launch
import java.util.Calendar

class AddMedicinesViewModel(application: Application) : AndroidViewModel(application) {

    private var context = getApplication<Application>().applicationContext
    private var medicinesNotificationService = MedicinesNotificationService(context)

    fun insertMedicine(params : MedicineParams) {
        viewModelScope.launch {
            val insertMedicines = InsertMedicine(repository = Repositories.medicinesRepository)
            val medicine = Medicines(
                id = 0,
                title = params.title,
                dateStart = Calendar.getInstance().timeInMillis,
                durationOfCourse = params.durationOfCourse,
                currentDayOfCourse = 1,
                timeOfNotify1 = params.timeOfNotify1,
                channelIDFirstTime = RandomUtil.getRandomInt(),
                timeOfNotify2 = params.timeOfNotify2,
                channelIDSecondTime = RandomUtil.getRandomInt(),
                timeOfNotify3 = params.timeOfNotify3,
                channelIDThirdTime = RandomUtil.getRandomInt(),
                timeOfNotify4 = params.timeOfNotify4,
                channelIDFourthTime = RandomUtil.getRandomInt(),
                totalMissed = 0,
            )
            insertMedicines.execute(medicine)
            createNotification(medicine)
        }
    }

    private fun createNotification(medicine : Medicines) {
        if (medicine.timeOfNotify1 != 0L) {
            medicinesNotificationService.setRepetitiveAlarm(
                medicine.timeOfNotify1,
                "Первый приём - " + medicine.title,
                medicine.channelIDFirstTime
            )
        }

        if (medicine.timeOfNotify2 != 0L) {
            medicinesNotificationService.setRepetitiveAlarm(
                medicine.timeOfNotify2,
                "Второй приём - " + medicine.title,
                medicine.channelIDSecondTime
            )
        }

        if (medicine.timeOfNotify3 != 0L) {
            medicinesNotificationService.setRepetitiveAlarm(
                medicine.timeOfNotify3,
                "Третий приём - " + medicine.title,
                medicine.channelIDThirdTime
            )
        }
        if (medicine.timeOfNotify4 != 0L) {
            medicinesNotificationService.setRepetitiveAlarm(
                medicine.timeOfNotify4,
                "Четвёртый приём - " + medicine.title,
                medicine.channelIDFourthTime
            )
        }

    }


}