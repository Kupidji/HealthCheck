package com.example.healthcheck.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.healthcheck.model.medicines.room.MedicinesDao
import com.example.healthcheck.model.medicines.room.entities.MedicinesDbEntity
import com.example.healthcheck.model.sleep.room.SleepDao
import com.example.healthcheck.model.sleep.room.entities.SleepDbEntity
import com.example.healthcheck.model.steps.room.StepsDao
import com.example.healthcheck.model.steps.room.entities.StepsDbEntity
import com.example.healthcheck.model.weight.room.WeightDao
import com.example.healthcheck.model.weight.room.entities.WeightDbEntity

@Database (
    version = 1,
    entities = [
        MedicinesDbEntity::class,
        SleepDbEntity::class,
        StepsDbEntity::class,
        WeightDbEntity::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMedicinesDao() : MedicinesDao

    abstract fun getSleepDao() : SleepDao

    abstract fun getStepsDao() : StepsDao

    abstract fun getWeightDao() : WeightDao

}