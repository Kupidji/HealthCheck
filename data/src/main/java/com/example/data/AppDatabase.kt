package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.heart.room.HeartDao
import com.example.data.heart.room.entities.HeartDbEntity
import com.example.data.medicines.room.MedicinesDao
import com.example.data.medicines.room.entities.MedicinesDbEntity
import com.example.data.sleep.room.SleepDao
import com.example.data.sleep.room.entities.SleepDbEntity
import com.example.data.steps.room.StepsDao
import com.example.data.steps.room.entities.StepsDbEntity
import com.example.data.weight.room.WeightDao
import com.example.data.weight.room.entities.WeightDbEntity


@Database (
    version = 1,
    entities = [
        MedicinesDbEntity::class,
        SleepDbEntity::class,
        StepsDbEntity::class,
        WeightDbEntity::class,
        HeartDbEntity::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMedicinesDao() : MedicinesDao

    abstract fun getSleepDao() : SleepDao

    abstract fun getStepsDao() : StepsDao

    abstract fun getWeightDao() : WeightDao

    abstract fun getHeartDao() : HeartDao

}