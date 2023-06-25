package com.example.healthcheck.model.sleep.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.healthcheck.model.sleep.room.entities.SleepDbEntity

@Dao
interface SleepDao {

    @Insert
    suspend fun insertTimeOfSleep(sleepDbEntity: SleepDbEntity)

    @Update
    suspend fun updateTimeOfSleep(sleepDbEntity: SleepDbEntity)

    @Query("SELECT * FROM Sleep ORDER BY id DESC LIMIT 7")
    fun getTimeOfSleepForWeek() : List<SleepDbEntity>

    @Query("SELECT * FROM Sleep ORDER BY id DESC LIMIT 30")
    fun getTimeOfSleepForMonth() : List<SleepDbEntity>

    @Query("SELECT * FROM Sleep ORDER BY id DESC LIMIT 1")
    fun getTimeOfSleepForDay() : List<SleepDbEntity>

}