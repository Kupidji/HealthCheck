package com.example.healthcheck.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.healthcheck.model.medicines.room.MedicinesDao
import com.example.healthcheck.model.medicines.room.entities.MedicinesDbEntity
import com.example.healthcheck.model.sleep.room.SleepDao
import com.example.healthcheck.model.sleep.room.entities.SleepDbEntity

@Database (
    version = 1,
    entities = [
        MedicinesDbEntity::class,
        SleepDbEntity::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMedicinesDao() : MedicinesDao

    abstract fun getSleepDao() : SleepDao

}