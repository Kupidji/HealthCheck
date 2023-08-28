package com.example.data.sleep.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.sleep.room.entities.SleepDbEntity

@Dao
interface SleepDao {

    @Insert
    suspend fun insertTimeOfSleep(sleepDbEntity: SleepDbEntity)

    @Update
    suspend fun updateTimeOfSleep(sleepDbEntity: SleepDbEntity)

    @Query("SELECT * FROM Sleep ORDER BY id DESC LIMIT 1")
    fun getTimeOfSleepForDay() : SleepDbEntity

    @Query("SELECT * FROM Sleep ORDER BY id DESC LIMIT 7")
    fun getTimeOfSleepForWeek() : List<SleepDbEntity>

    @Query("SELECT * FROM Sleep ORDER BY id DESC LIMIT 30")
    fun getTimeOfSleepForMonth() : List<SleepDbEntity>

    @Query("SELECT * FROM Sleep")
    fun getSleepListForHistory() : List<SleepDbEntity>

}