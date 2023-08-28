package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetWeightForMonthFromDb(private val repository: WeightRepository) {
    suspend fun execute() : Flow<Float> = withContext(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext repository.getWeightForMonth()
        }
        return@withContext list.map { listOfWeight ->
            var sum = 0F
            for (weight in listOfWeight) {
                sum += weight
            }
            if (listOfWeight.isNotEmpty()) {
                sum/listOfWeight.size
            }
            else {
                0F
            }
        }
    }

//    suspend fun getWeightFromDataForMonth() : Float {
//        var result = viewModelScope.async(AppDispatchers.default) {
//            var list = withContext(AppDispatchers.io) {
//                var listOfWeight = Repositories.weightRepository.getWeightForMonth()
//                return@withContext listOfWeight
//            }
//            var sum = 0F
//
//            for (steps in list) {
//                sum += steps
//            }
//
//            if (list.isNotEmpty()){
//                sum /= list.size
//            }
//
//            return@async sum
//        }
//
//        return  result.await()
//    }

}