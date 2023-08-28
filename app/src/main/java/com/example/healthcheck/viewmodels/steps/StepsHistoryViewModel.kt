package com.example.healthcheck.viewmodels.steps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Steps
import com.example.domain.usecase.steps.GetStepsListForHistory
import com.example.domain.usecase.steps.UpdateSteps
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class StepsHistoryViewModel : ViewModel() {

    private val _stepsListHistory = MutableSharedFlow<List<Steps>>(1, 0, BufferOverflow.DROP_OLDEST)
    val stepsListHistory = _stepsListHistory.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getStepsListForHistory = GetStepsListForHistory(repository = Repositories.stepsRepository)
            getStepsListForHistory.execute().collect { list ->
                _stepsListHistory.emit(list)
            }

        }
    }

    fun updateSteps(steps : Steps, countOfSteps : Int) {
        viewModelScope.launch {
            val updateSteps = UpdateSteps(repository = Repositories.stepsRepository)
            val newSteps = Steps(
                id = steps.id,
                countOfSteps = countOfSteps,
                date = steps.date
            )
            updateSteps.execute(steps = newSteps)
        }
    }

}