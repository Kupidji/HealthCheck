package com.example.healthcheck.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.healthcheck.model.medicines.room.MedicinesDao
import com.example.healthcheck.model.medicines.room.entities.MedicinesDbEntity

@Database (
    version = 1,
    entities = [MedicinesDbEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMedicinesDao() : MedicinesDao

}