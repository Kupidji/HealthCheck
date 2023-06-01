package com.example.healthcheck.model.medicines

import androidx.lifecycle.LiveData
import com.example.healthcheck.model.medicines.entities.Medicines

interface MedicinesRepository {

    fun getAllMedicines() : LiveData<List<Medicines>>

    suspend fun createMedicine(medicines : Medicines)

    suspend fun updateMedicine(medicines : Medicines)

    suspend fun deleteMedicine(medicines: Medicines)

}