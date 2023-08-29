package com.example.domain.usecase.heart

import com.example.domain.AppDispatchers
import com.example.domain.models.Heart
import com.example.domain.repository.HeartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetLastHeartIdAndDate(private val repository: HeartRepository) {

    suspend fun execute() : Flow<Heart> = withContext(AppDispatchers.io) {
        return@withContext repository.getCardioForDay().map { list ->
            var ourHeart = Heart(0,0,0,0,0)
            for (heart in list) {
                ourHeart = heart
            }
            ourHeart
        }
    }

}