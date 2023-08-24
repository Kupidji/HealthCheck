package com.example.domain.usecase.profile

import com.example.domain.AppDispatchers
import com.example.domain.repository.ProfileStorageRepository
import kotlinx.coroutines.withContext

class SetHeight(private val repository: ProfileStorageRepository) {

    suspend fun execute(height : Float) = withContext(AppDispatchers.io){
        repository.setHeight(height = height)
    }

}