package com.example.data.medicines.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.medicines.entities.MedicinesForDb
import com.example.domain.models.Medicines


@Entity (
    tableName = "medicines"
)
data class MedicinesDbEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val title : String,
    @ColumnInfo(name = "date_start")
    var dateStart : Long,
    @ColumnInfo(name = "duration_of_course")
    var durationOfCourse : Int,
    @ColumnInfo(name = "current_day_of_course")
    var currentDayOfCourse : Int,
    @ColumnInfo(name = "time_of_notify1")
    var timeOfNotify1 : Long,
    @ColumnInfo(name = "channel_id_first_time")
    var channelIDFirstTime : Int,
    @ColumnInfo(name = "time_of_notify2")
    var timeOfNotify2 : Long,
    @ColumnInfo(name = "channel_id_second_time")
    var channelIDSecondTime : Int,
    @ColumnInfo(name = "time_of_notify3")
    var timeOfNotify3 : Long,
    @ColumnInfo(name = "channel_id_third_time")
    var channelIDThirdTime : Int,
    @ColumnInfo(name = "time_of_notify4")
    var timeOfNotify4 : Long,
    @ColumnInfo(name = "channel_id_fourth_time")
    var channelIDFourthTime : Int,
    @ColumnInfo(name = "total_missed")
    var totalMissed : Int,
) {
    fun toMedicines() : Medicines = Medicines(
        id = id,
        title = title,
        dateStart = dateStart,
        durationOfCourse = durationOfCourse,
        currentDayOfCourse = currentDayOfCourse,
        timeOfNotify1 = timeOfNotify1,
        channelIDFirstTime = channelIDFirstTime,
        timeOfNotify2 = timeOfNotify2,
        channelIDSecondTime = channelIDSecondTime,
        timeOfNotify3 = timeOfNotify3,
        channelIDThirdTime = channelIDThirdTime,
        timeOfNotify4 = timeOfNotify4,
        channelIDFourthTime = channelIDFourthTime,
        totalMissed = totalMissed,
    )

    companion object {
        fun fromAddMedicines(medicines: MedicinesForDb) : MedicinesDbEntity = MedicinesDbEntity(
            id = 0,
            title = medicines.title,
            durationOfCourse = medicines.durationOfCourse,
            currentDayOfCourse = medicines.currentDayOfCourse,
            dateStart = medicines.dateStart,
            timeOfNotify1 = medicines.timeOfNotify1,
            channelIDFirstTime = medicines.channelIDFirstTime,
            timeOfNotify2 = medicines.timeOfNotify2,
            channelIDSecondTime = medicines.channelIDSecondTime,
            timeOfNotify3 = medicines.timeOfNotify3,
            channelIDThirdTime = medicines.channelIDThirdTime,
            timeOfNotify4 = medicines.timeOfNotify4,
            channelIDFourthTime = medicines.channelIDFourthTime,
            totalMissed = medicines.totalMissed,
        )

        fun fromEditMedicines(medicines: MedicinesForDb) : MedicinesDbEntity = MedicinesDbEntity(
            id = medicines.id,
            title = medicines.title,
            durationOfCourse = medicines.durationOfCourse,
            currentDayOfCourse = medicines.currentDayOfCourse,
            dateStart = medicines.dateStart,
            timeOfNotify1 = medicines.timeOfNotify1,
            channelIDFirstTime = medicines.channelIDFirstTime,
            timeOfNotify2 = medicines.timeOfNotify2,
            channelIDSecondTime = medicines.channelIDSecondTime,
            timeOfNotify3 = medicines.timeOfNotify3,
            channelIDThirdTime = medicines.channelIDThirdTime,
            timeOfNotify4 = medicines.timeOfNotify4,
            channelIDFourthTime = medicines.channelIDFourthTime,
            totalMissed = medicines.totalMissed,
        )
    }

}