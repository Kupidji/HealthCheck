package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.models.Medicines
import com.example.domain.repository.MedicinesRepository
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class GetNearestAction(private val repository : MedicinesRepository) {

    lateinit var nearestAction : Medicines
    var nearestActionText = ""

    suspend fun execute() = withContext(AppDispatchers.default){
        val listOfMedicines = withContext(AppDispatchers.io) {
            return@withContext repository.getAllMedicineList()
        }

        var nearestActionTrue = Medicines(
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
        var tempDiffTimeTrue = 86400000L
        var tempNearestNotificationTrue = 0L

        var nearestActionFalse = Medicines(
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
        var tempDiffTimeFalse = 86400000L
        var tempNearestNotificationFalse = 0L

        for (medicine in listOfMedicines) {
            var listOfNotifications = listOf (
                medicine.timeOfNotify1,
                medicine.timeOfNotify2,
                medicine.timeOfNotify3,
                medicine.timeOfNotify4,
            )
            val currentTimeTrue = Calendar.getInstance().timeInMillis
            var currentTimeFalse = 0L
            for (notification in listOfNotifications) {
                if (notification != 0L) {
                    //Если текущее время меньше чем действие -> действие будущее, иначе прошедшее
                    if (currentTimeTrue - notification < 0) {
                        val notificationDiffTime = notification - currentTimeTrue
                        if (notificationDiffTime < tempDiffTimeTrue) {
                            tempDiffTimeTrue = notificationDiffTime
                            tempNearestNotificationTrue = notification
                            nearestActionTrue = medicine
                        }
                    }
                    else {
                        currentTimeFalse = getFakeCurrentTime(currentTimeTrue, notification)
                        if (currentTimeFalse - notification < 0) {
                            val notificationDiffTime = notification - currentTimeFalse
                            if (notificationDiffTime < tempDiffTimeFalse) {
                                tempDiffTimeFalse = notificationDiffTime
                                tempNearestNotificationFalse = notification
                                nearestActionFalse = medicine
                            }
                        }
                    }
                }
            }
        }

        if (nearestActionTrue.title != "") {
            nearestAction = nearestActionTrue
            nearestActionText = nearestActionTrue.title + " - " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(tempNearestNotificationTrue)+", через " + showTime(tempDiffTimeTrue)
        }
        else {
            nearestAction = nearestActionFalse
            if (nearestActionFalse.title != "") {
                nearestActionText = nearestActionFalse.title + " - " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(tempNearestNotificationFalse)+", через " + showTime(tempDiffTimeFalse)
            }
        }

    }

    private fun getFakeCurrentTime(curTime : Long, actionTime : Long) : Long {
        var result = curTime
        val diffBetweenCurDateAndActionDate : Int = ((curTime - actionTime).toFloat()/86400000F).toBigDecimal().setScale(0, RoundingMode.UP).toInt()
        if ((curTime - actionTime) > 0) {
            result -= (86400000 * diffBetweenCurDateAndActionDate)
        }
        return result
    }

    private fun showTime(totalTime : Long) : String {
        val hours = Math.abs((totalTime) / 1000 / 60 / 60).toString()
        var minutes = Math.abs((totalTime) / 1000 / 60 % 60).toString()
        if (minutes.length % 2 != 0)
            minutes = "0$minutes"
        return "$hours:$minutes"
    }

}