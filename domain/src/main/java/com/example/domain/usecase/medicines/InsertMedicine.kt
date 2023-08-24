package com.example.domain.usecase.medicines

import com.example.domain.AppDispatchers
import com.example.domain.models.Medicines
import com.example.domain.repository.MedicinesRepository
import kotlinx.coroutines.withContext

class InsertMedicine(private val repository: MedicinesRepository) {

    suspend fun execute(medicine : Medicines) = withContext(AppDispatchers.io) {
        repository.createMedicine(medicines = medicine)
    }

}