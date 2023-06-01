package com.example.healthcheck.model

import android.content.Context
import androidx.room.Room
import com.example.healthcheck.model.medicines.MedicinesRepository
import com.example.healthcheck.model.medicines.room.RoomMedicinesRepository

object Repositories {

    private lateinit var applicationContext : Context

    private val database : AppDatabase by lazy<AppDatabase> {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "healtcheck.db")
            .build()
    }

    val medicinesRepository : MedicinesRepository by lazy {
        RoomMedicinesRepository(database.getMedicinesDao())
    }

    fun init(context : Context) {
        applicationContext = context
    }

}