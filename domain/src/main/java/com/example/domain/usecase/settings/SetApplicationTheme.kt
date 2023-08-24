package com.example.domain.usecase.settings

import com.example.domain.AppDispatchers
import com.example.domain.repository.SettingsStorageRepository
import kotlinx.coroutines.withContext

class SetApplicationTheme(private val repository: SettingsStorageRepository) {

    suspend fun execute(theme : Int) = withContext(AppDispatchers.io) {
        repository.setApplicationTheme(theme = theme)
    }

}