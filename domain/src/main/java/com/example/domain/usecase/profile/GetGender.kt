package com.example.domain.usecase.profile

import com.example.domain.AppDispatchers
import com.example.domain.repository.ProfileStorageRepository
import kotlinx.coroutines.withContext

class GetGender(private val repository: ProfileStorageRepository) {

    suspend fun execute() = withContext(AppDispatchers.io) {
        return@withContext repository.getGender()
    }

}