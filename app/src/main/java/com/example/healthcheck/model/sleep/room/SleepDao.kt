package com.example.healthcheck.model.sleep.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.healthcheck.model.sleep.room.entities.SleepDbEntity

@Dao
interface SleepDao {

    @Insert
    suspend fun insertTimeOfSleep(sleepDbEntity: SleepDbEntity)

    //TODO доделать запрос для вытягивание инфы за неделю
    @Query("SELECT * FROM sleep")
    fun getTimeOfSleepForWeek() : List<SleepDbEntity>

    //TODO доделать запрос для вытягивание инфы за месяц
    @Query("SELECT * FROM sleep")
    fun getTimeOfSleepForMonth() : List<SleepDbEntity>

}