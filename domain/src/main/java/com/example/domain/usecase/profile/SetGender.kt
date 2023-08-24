package com.example.domain.usecase.profile

import com.example.domain.AppDispatchers
import com.example.domain.repository.ProfileStorageRepository
import kotlinx.coroutines.withContext

class SetGender(private val repository: ProfileStorageRepository) {

    suspend fun execute(gender : Boolean) = withContext(AppDispatchers.io) {
        repository.setGender(gender = gender)
    }

}