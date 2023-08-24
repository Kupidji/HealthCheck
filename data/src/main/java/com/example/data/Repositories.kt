package com.example.data

import android.content.Context
import androidx.room.Room
import com.example.domain.repository.HeartRepository
import com.example.data.heart.room.RoomHeartRepository
import com.example.data.medicines.room.RoomMedicinesRepository
import com.example.data.profile.SharedPrefProfileStorage
import com.example.data.settings.SharedPrefSettingsStorage
import com.example.data.sleep.SharedPrefSleepStorage
import com.example.domain.repository.SleepRepository
import com.example.data.sleep.room.RoomSleepRepository
import com.example.data.steps.SharedPrefStepsStorage
import com.example.domain.repository.StepsRepository
import com.example.data.steps.room.RoomStepsRepository
import com.example.data.weight.SharedPrefWeightStorage
import com.example.domain.repository.WeightRepository
import com.example.data.weight.room.RoomWeightRepository
import com.example.domain.repository.MedicinesRepository
import com.example.domain.repository.ProfileStorageRepository
import com.example.domain.repository.SettingsStorageRepository
import com.example.domain.repository.SleepStorageRepository
import com.example.domain.repository.StepsStorageRepository
import com.example.domain.repository.WeightStorageRepository


object Repositories {

    private lateinit var applicationContext : Context

    private val database : AppDatabase by lazy<AppDatabase> {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "healtcheck.db")
            .build()
    }

    val profileStorage : ProfileStorageRepository by lazy {
        SharedPrefProfileStorage(context = applicationContext)
    }

    val settingsStorage : SettingsStorageRepository by lazy {
        SharedPrefSettingsStorage(context = applicationContext)
    }

    val stepsRepository : StepsRepository by lazy {
        RoomStepsRepository(stepsDao = database.getStepsDao())
    }

    val stepsStorage : StepsStorageRepository by lazy {
        SharedPrefStepsStorage(context = applicationContext)
    }

    val sleepRepository : SleepRepository by lazy {
        RoomSleepRepository(sleepDao = database.getSleepDao())
    }

    val sleepStorage : SleepStorageRepository by lazy {
        SharedPrefSleepStorage(context = applicationContext)
    }

    val heartRepository : HeartRepository by lazy {
        RoomHeartRepository(heartDao = database.getHeartDao())
    }

    val weightRepository : WeightRepository by lazy {
        RoomWeightRepository(weightDao =  database.getWeightDao())
    }

    val weightStorage : WeightStorageRepository by lazy {
        SharedPrefWeightStorage(context = applicationContext)
    }

    val medicinesRepository : MedicinesRepository by lazy {
        RoomMedicinesRepository(medicinesDao = database.getMedicinesDao())
    }

    fun init(context : Context) {
        applicationContext = context
    }

}