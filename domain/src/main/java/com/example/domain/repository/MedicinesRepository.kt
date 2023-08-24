package com.example.domain.repository


import com.example.domain.models.Medicines
import kotlinx.coroutines.flow.Flow

interface MedicinesRepository {

    fun getAllMedicines() : Flow<List<Medicines>>

    suspend fun getAllMedicineList() : List<Medicines>

    suspend fun createMedicine(medicines : Medicines)

    suspend fun updateMedicine(medicines : Medicines)

    suspend fun deleteMedicine(medicines: Medicines)

}