package com.example.domain.usecase.start

import com.example.domain.AppDispatchers
import com.example.domain.repository.SettingsStorageRepository
import kotlinx.coroutines.withContext

class SetTimeOfRegularNotification(private val repository: SettingsStorageRepository) {

    suspend fun execute(time : Long) = withContext(AppDispatchers.io) {
        repository.setTimeOfReminderNotification(time = time)
    }

}