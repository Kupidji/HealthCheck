package com.example.data.sleep.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.sleep.entities.SleepForDb


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

    fun toSleepForDb() : SleepForDb = SleepForDb(
        id = id,
        timeOfSleep = timeOfSleep,
        date = date,
    )

    companion object {

        fun fromSleep(sleep : SleepForDb) = SleepDbEntity(
            id = 0,
            timeOfSleep = sleep.timeOfSleep,
            date = sleep.date,
        )

        fun updateSleep(sleep : SleepForDb) = SleepDbEntity(
            id = sleep.id,
            timeOfSleep = sleep.timeOfSleep,
            date = sleep.date,
        )
    }
}