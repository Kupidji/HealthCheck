package com.example.domain.usecase.profile

import com.example.domain.AppDispatchers
import com.example.domain.repository.ProfileStorageRepository
import kotlinx.coroutines.withContext

class GetHeight(private val repository: ProfileStorageRepository) {

    suspend fun execute() : Float = withContext(AppDispatchers.io){
        return@withContext repository.getHeight()
    }

}