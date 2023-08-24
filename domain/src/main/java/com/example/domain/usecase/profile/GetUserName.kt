package com.example.domain.usecase.profile

import com.example.domain.AppDispatchers
import com.example.domain.repository.ProfileStorageRepository
import kotlinx.coroutines.withContext

class GetUserName(private val repository: ProfileStorageRepository) {

    suspend fun execute() : String = withContext(AppDispatchers.io) {
        return@withContext repository.getUserName()
    }

}