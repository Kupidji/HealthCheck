package com.example.domain.usecase.settings

import com.example.domain.AppDispatchers
import com.example.domain.repository.SettingsStorageRepository
import kotlinx.coroutines.withContext

class SetNutritionVisibility(private val repository : SettingsStorageRepository) {

    suspend fun execute(visibility: Boolean) = withContext(AppDispatchers.io) {
        repository.setNutritionVisibility(visibility = visibility)
    }

}