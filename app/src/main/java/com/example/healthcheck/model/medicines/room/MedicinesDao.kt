package com.example.healthcheck.model.medicines.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.healthcheck.model.medicines.room.entities.MedicinesDbEntity

@Dao
interface MedicinesDao {

    @Insert
    suspend fun createMedicine(medicinesDbEntity: MedicinesDbEntity)

    @Query("SELECT * FROM medicines")
    fun getAllMedicine() : LiveData<List<MedicinesDbEntity>>

    @Update(entity = MedicinesDbEntity::class)
    suspend fun updateMedicine(medicinesDbEntity: MedicinesDbEntity)

    @Delete
    suspend fun deleteMedicine(medicinesDbEntity: MedicinesDbEntity)

}