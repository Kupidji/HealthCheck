package com.example.domain.repository

import com.example.domain.models.Sleep

interface SleepRepository {

    suspend fun insertTimeOfSleep(sleep : Sleep)

    suspend fun updateTimeOfSleep(sleep: Sleep)

    suspend fun getTimeOfSleepForDay() : Long

    suspend fun getTimeOfSleepForWeek() : List<Long>

    suspend fun getTimeOfSleepForMonth() : List<Long>

    suspend fun getListForHistory() : List<Sleep>

    suspend fun getLastDate() : Sleep

}