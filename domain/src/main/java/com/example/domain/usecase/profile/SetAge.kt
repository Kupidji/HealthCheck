package com.example.domain.usecase.profile

import com.example.domain.AppDispatchers
import com.example.domain.repository.ProfileStorageRepository
import kotlinx.coroutines.withContext

class SetAge(private val repository: ProfileStorageRepository) {

    suspend fun execute(age : Int) = withContext(AppDispatchers.io) {
        repository.setAge(age = age)
    }

}