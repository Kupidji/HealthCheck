package com.example.domain.usecase.start

import com.example.domain.AppDispatchers
import com.example.domain.repository.SettingsStorageRepository
import kotlinx.coroutines.withContext

class SetFirstLaunchCompleted(private val repository: SettingsStorageRepository) {

    suspend fun execute(completed : Boolean) = withContext(AppDispatchers.io) {
        repository.setFirstLaunchCompleted(completed = completed)
    }

}