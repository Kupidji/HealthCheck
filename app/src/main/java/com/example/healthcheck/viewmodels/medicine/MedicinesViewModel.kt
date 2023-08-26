package com.example.healthcheck.viewmodels.medicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Medicines
import com.example.domain.usecase.medicines.GetAllMedicinesFlow
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MedicinesViewModel : ViewModel() {

    private val _listOfMedicines = MutableSharedFlow<List<Medicines>>(1, 0, BufferOverflow.DROP_OLDEST)
    val listOfMedicines = _listOfMedicines.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getAllMedicinesFlow = GetAllMedicinesFlow(repository = Repositories.medicinesRepository)
            getAllMedicinesFlow.execute().collect { list ->
                _listOfMedicines.emit(list)
            }
        }

    }

}