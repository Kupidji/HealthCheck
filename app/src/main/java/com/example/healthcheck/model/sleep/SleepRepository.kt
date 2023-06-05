package com.example.healthcheck.model.sleep

import com.example.healthcheck.model.sleep.entities.Sleep

interface SleepRepository {

    suspend fun insertTimeOfSleep(sleep : Sleep)

    fun getTimeOfSleepForWeek() : List<Sleep>

    fun getTimeOfSleepForMonth() : List<Sleep>

}