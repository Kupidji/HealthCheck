package com.example.data.medicines.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.medicines.room.entities.MedicinesDbEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MedicinesDao {

    @Insert
    suspend fun createMedicine(medicinesDbEntity: MedicinesDbEntity)

    @Query("SELECT * FROM medicines")
    fun getAllMedicine() : Flow<List<MedicinesDbEntity>>

    @Query("SELECT * FROM medicines")
    suspend fun getAllMedicineList() : List<MedicinesDbEntity>

    @Update
    suspend fun updateMedicine(medicinesDbEntity: MedicinesDbEntity)

    @Delete
    suspend fun deleteMedicine(medicinesDbEntity: MedicinesDbEntity)

}