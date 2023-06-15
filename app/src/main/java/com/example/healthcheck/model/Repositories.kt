package com.example.healthcheck.model

import android.content.Context
import androidx.room.Room
import com.example.healthcheck.model.heart.HeartRepository
import com.example.healthcheck.model.heart.room.RoomHeartRepository
import com.example.healthcheck.model.medicines.MedicinesRepository
import com.example.healthcheck.model.medicines.room.RoomMedicinesRepository
import com.example.healthcheck.model.sleep.SleepRepository
import com.example.healthcheck.model.sleep.room.RoomSleepRepository
import com.example.healthcheck.model.steps.StepsRepository
import com.example.healthcheck.model.steps.room.RoomStepsRepository
import com.example.healthcheck.model.weight.WeightRepository
import com.example.healthcheck.model.weight.room.RoomWeightRepository

object Repositories {

    private lateinit var applicationContext : Context

    private val database : AppDatabase by lazy<AppDatabase> {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "healtcheck.db")
            .build()
    }

    val medicinesRepository : MedicinesRepository by lazy {
        RoomMedicinesRepository(database.getMedicinesDao())
    }

    val sleepRepository : SleepRepository by lazy {
        RoomSleepRepository(database.getSleepDao())
    }

    val stepsRepository : StepsRepository by lazy {
        RoomStepsRepository(database.getStepsDao())
    }

    val weightRepository : WeightRepository by lazy {
        RoomWeightRepository(database.getWeightDao())
    }

    val heartRepository : HeartRepository by lazy {
        RoomHeartRepository(database.getHeartDao())
    }

    fun init(context : Context) {
        applicationContext = context
    }

}