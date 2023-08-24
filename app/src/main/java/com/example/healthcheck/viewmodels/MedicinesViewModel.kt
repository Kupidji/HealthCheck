package com.example.healthcheck.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Medicines
import com.example.domain.usecase.medicines.GetAllMedicinesFlow
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class MedicinesViewModel : ViewModel() {

    private val _listOfMedicines = MutableSharedFlow<List<Medicines>>(1, 0, BufferOverflow.DROP_OLDEST)
    val listOfMedicines = _listOfMedicines.asSharedFlow()

    private val _countOfMedicines = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val countOfMedicines = _countOfMedicines.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getAllMedicinesFlow = GetAllMedicinesFlow(repository = Repositories.medicinesRepository)
            getAllMedicinesFlow.execute().collect { list ->
                _listOfMedicines.emit(list)
                _countOfMedicines.emit(list.size)
            }
        }

    }

//    fun getAllMedicines() : SharedFlow<List<Medicines>> {
//        return Repositories.medicinesRepository.getAllMedicines().shareIn(viewModelScope, SharingStarted.Eagerly, 1)
//    }

}