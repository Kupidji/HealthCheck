package com.example.domain.usecase.settings

import com.example.domain.AppDispatchers
import com.example.domain.repository.SettingsStorageRepository
import kotlinx.coroutines.withContext

class GetApplicationTheme(private val repository : SettingsStorageRepository) {

    suspend fun execute() : Int = withContext(AppDispatchers.io) {
        return@withContext repository.getApplicationTheme()
    }

}