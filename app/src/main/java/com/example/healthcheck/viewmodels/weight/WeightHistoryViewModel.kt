package com.example.healthcheck.viewmodels.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Weight
import com.example.domain.usecase.weight.GetWeightListForHistory
import com.example.domain.usecase.weight.UpdateWeight
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WeightHistoryViewModel : ViewModel() {

    private val _weightListHistory = MutableSharedFlow<List<Weight>>(1, 0, BufferOverflow.DROP_OLDEST)
    val weightListHistory = _weightListHistory.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getWeightListForHistory = GetWeightListForHistory(repository = Repositories.weightRepository)
            getWeightListForHistory.execute().collect { list ->
                _weightListHistory.emit(list)
            }
        }
    }

    fun updateWeight(weight : Weight, value : Float) {
        viewModelScope.launch {
            val updateWeight = UpdateWeight(repository = Repositories.weightRepository)
            val ourWeight = Weight (
                id = weight.id,
                weight = value,
                date = weight.date
            )
            updateWeight.execute(ourWeight)
        }
    }

}