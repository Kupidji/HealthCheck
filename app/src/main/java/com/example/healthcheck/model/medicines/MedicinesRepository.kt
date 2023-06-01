package com.example.healthcheck.model.medicines

import com.example.healthcheck.model.medicines.entities.Medicines
import kotlinx.coroutines.flow.Flow

interface MedicinesRepository {

    fun getAllMedicines() : Flow<List<Medicines>>

    suspend fun createMedicine(medicines : Medicines)

    suspend fun updateMedicine(medicines : Medicines)

    suspend fun deleteMedicine(medicines: Medicines)

}