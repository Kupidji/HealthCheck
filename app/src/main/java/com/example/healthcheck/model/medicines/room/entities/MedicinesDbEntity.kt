package com.example.healthcheck.model.medicines.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.healthcheck.model.medicines.entities.Medicines
import java.time.LocalDate
import java.util.Calendar

@Entity (
    tableName = "medicines"
)
data class MedicinesDbEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val title : String,
    @ColumnInfo(name = "date_start")
    var dateStart : Int,
    @ColumnInfo(name = "duration_of_course")
    var durationOfCourse : Int,
    @ColumnInfo(name = "time_of_notify1")
    var timeOfNotify1 : Long,
    @ColumnInfo(name = "time_of_notify2")
    var timeOfNotify2 : Long,
    @ColumnInfo(name = "time_of_notify3")
    var timeOfNotify3 : Long,
    @ColumnInfo(name = "time_of_notify4")
    var timeOfNotify4 : Long,
) {
    fun toMedicines() : Medicines = Medicines(
        title = title,
        dateStart = dateStart,
        durationOfCourse = durationOfCourse,
        timeOfNotify1 = timeOfNotify1,
        timeOfNotify2 = timeOfNotify2,
        timeOfNotify3 = timeOfNotify3,
        timeOfNotify4 = timeOfNotify4,
    )

    companion object {
        val calendar = Calendar.getInstance()

        fun fromAddMedicines(medicines: Medicines) : MedicinesDbEntity = MedicinesDbEntity(
            id = 0,
            title = medicines.title,
            durationOfCourse = medicines.durationOfCourse,
            dateStart = calendar.get(Calendar.DAY_OF_MONTH),
            timeOfNotify1 = medicines.timeOfNotify1,
            timeOfNotify2 = medicines.timeOfNotify2,
            timeOfNotify3 = medicines.timeOfNotify3,
            timeOfNotify4 = medicines.timeOfNotify4,
        )
    }

}