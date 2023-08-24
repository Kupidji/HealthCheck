package com.example.domain.usecase.settings

import com.example.domain.AppDispatchers
import com.example.domain.repository.SettingsStorageRepository
import kotlinx.coroutines.withContext

class GetNutritionVisibility(private val repository : SettingsStorageRepository) {

    suspend fun execute() : Boolean = withContext(AppDispatchers.io) {
        return@withContext repository.getNutritionVisibility()
    }

}