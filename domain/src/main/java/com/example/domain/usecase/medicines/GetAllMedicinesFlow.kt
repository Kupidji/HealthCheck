package com.example.domain.usecase.medicines

import com.example.domain.AppDispatchers
import com.example.domain.models.Medicines
import com.example.domain.repository.MedicinesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetAllMedicinesFlow(private val repository: MedicinesRepository) {

    suspend fun execute() : Flow<List<Medicines>> = withContext(AppDispatchers.io) {
        return@withContext repository.getAllMedicines()
    }

}