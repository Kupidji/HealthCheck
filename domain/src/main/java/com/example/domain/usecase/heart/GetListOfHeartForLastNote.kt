package com.example.domain.usecase.heart

import com.example.domain.AppDispatchers
import com.example.domain.models.Heart
import com.example.domain.repository.HeartRepository
import kotlinx.coroutines.withContext

class GetListOfHeartForLastNote(private val repository : HeartRepository) {

    suspend fun execute() : List<Heart> = withContext(AppDispatchers.io) {
        return@withContext repository.getListCardioForLastNote()
    }

}