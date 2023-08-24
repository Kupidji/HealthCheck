package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightRepository
import kotlinx.coroutines.withContext

class GetWeightForWeekFromDb(private val repository: WeightRepository) {

    suspend fun execute() : Float = withContext(AppDispatchers.default){
        val list = withContext(AppDispatchers.io) {
            return@withContext repository.getWeightForWeek()
        }
        var sum = 0F

        for (steps in list) {
            sum += steps
        }

        if (list.isNotEmpty()){
            sum /= list.size
        }

        return@withContext sum
    }


//    suspend fun getWeightFromDataForWeek() : Float {
//        var result = viewModelScope.async(AppDispatchers.io) {
//            var list = com.example.data.Repositories.weightRepository.getWeightForWeek()
//
//            var sumResult = withContext(AppDispatchers.default) {
//                var sum = 0F
//
//                for (steps in list) {
//                    sum += steps
//                }
//
//                if (list.isNotEmpty()){
//                    sum /= list.size
//                }
//                return@withContext sum
//            }
//            return@async sumResult
//        }
//        return result.await()
//    }

}