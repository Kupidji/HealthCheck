package com.example.healthcheck.model.sleep.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.healthcheck.model.sleep.entities.Sleep

@Entity (
    tableName = "sleep"
)
data class SleepDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name = "time_of_sleep")
    var timeOfSleep : Long,
    var date : Long,
) {

    fun toSleep() : Sleep = Sleep(
        timeOfSleep = timeOfSleep,
        date = date,
    )

    companion object {

        fun fromSleep(sleep : Sleep) = SleepDbEntity(
            id = 0,
            timeOfSleep = sleep.timeOfSleep,
            date = sleep.date,
        )

    }
}