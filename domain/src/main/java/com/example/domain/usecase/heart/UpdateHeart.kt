package com.example.domain.usecase.heart

import com.example.domain.AppDispatchers
import com.example.domain.models.Heart
import com.example.domain.repository.HeartRepository
import kotlinx.coroutines.withContext

class UpdateHeart(private val repository: HeartRepository) {

    suspend fun execute(heart : Heart) = withContext(AppDispatchers.io) {
        repository.updateCardio(heart = heart)
    }

}