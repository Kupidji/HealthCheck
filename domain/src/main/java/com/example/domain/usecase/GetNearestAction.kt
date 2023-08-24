package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.models.Medicines
import com.example.domain.repository.MedicinesRepository
import kotlinx.coroutines.withContext
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class GetNearestAction(private val repository : MedicinesRepository) {

    lateinit var _nearestAction : Medicines
    var _nearestTime = ""

    suspend fun execute() = withContext(AppDispatchers.default) {
        var nearestAction = Medicines(
            id = 0,
            title = "",
            dateStart = 0,
            durationOfCourse = 0,
            currentDayOfCourse = 0,
            timeOfNotify1 = 0,
            channelIDFirstTime = 0,
            timeOfNotify2 = 0,
            channelIDSecondTime = 0,
            timeOfNotify3 = 0,
            channelIDThirdTime = 0,
            timeOfNotify4 = 0,
            channelIDFourthTime = 0,
            totalMissed = 0,
        )

        var currentTime = Calendar.getInstance().timeInMillis
        var tempMin = currentTime
        var tempNearestTime = 0L

        val list = withContext(AppDispatchers.io) {
            return@withContext repository.getAllMedicineList()
        }

        for (medicine in list) {
            var listOfNotifications = listOf(
                medicine.timeOfNotify1,
                medicine.timeOfNotify2,
                medicine.timeOfNotify3,
                medicine.timeOfNotify4,
            )

            for (action in listOfNotifications) {
                var tempTime = abs(currentTime - action)
                if (tempTime < tempMin) {
                    tempMin = tempTime
                    tempNearestTime = action
                    nearestAction = medicine
                    nearestAction.title = medicine.title
                }
            }
        }

        _nearestAction = nearestAction
        if (nearestAction.title != "") {
            _nearestTime = nearestAction.title + " - " + SimpleDateFormat("HH:mm").format(tempNearestTime)+", через "+ forGmt(calculateTime(currentTime, tempNearestTime))
        }

    }

    private fun getGMT() : String {
        val tz = TimeZone.getDefault()
        val gmt1 = TimeZone.getTimeZone(tz.id).getDisplayName(false, TimeZone.SHORT)
        return if (gmt1.length > 3){
            gmt1.slice(4..8)
        } else{
            "00:00"
        }
    }

    private fun forGmt(long: Long) : String {
        val GMT = getGMT()
        val listGMT = GMT.split(":")
        return SimpleDateFormat("HH:mm").format(long - listGMT[0].toInt() * 3600000)
    }

    private fun calculateTime(time1 : Long, time2 : Long) : Long {
        return if (time1 - time2 <= 0) {
            time2 - time1
        } else {
            time2 + 86400000 - time1
        }
    }

}