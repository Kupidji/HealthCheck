package com.example.domain.usecase.profile

import com.example.domain.AppDispatchers
import com.example.domain.repository.ProfileStorageRepository
import kotlinx.coroutines.withContext

class SetUserName(private val repository: ProfileStorageRepository) {

    suspend fun execute(name : String) = withContext(AppDispatchers.io) {
        repository.setUserName(name = name)
    }

}