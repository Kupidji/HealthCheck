package com.example.domain.usecase.start

import com.example.domain.AppDispatchers
import com.example.domain.repository.SettingsStorageRepository
import kotlinx.coroutines.withContext

class GetTimeOfRegularNotification(private val repository: SettingsStorageRepository) {

    suspend fun execute() : Long = withContext(AppDispatchers.io) {
        return@withContext repository.getTimeOfReminderNotification()
    }

}