package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.repository.StepsStorageRepository
import kotlinx.coroutines.withContext

class SetStepsTarget(private val repository : StepsStorageRepository) {

    suspend fun execute(target : Int) =  withContext(AppDispatchers.io){
        repository.setTarget(target = target)
    }

}