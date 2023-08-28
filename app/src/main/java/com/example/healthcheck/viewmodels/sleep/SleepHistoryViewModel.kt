package com.example.healthcheck.viewmodels.sleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Sleep
import com.example.domain.usecase.sleep.GetSleepListForHistory
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SleepHistoryViewModel : ViewModel() {

    private val _sleepListHistory = MutableSharedFlow<List<Sleep>>(1, 0, BufferOverflow.DROP_OLDEST)
    val sleepListHistory = _sleepListHistory.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getSleepListForHistory = GetSleepListForHistory(repository = Repositories.sleepRepository)
            _sleepListHistory.emit(getSleepListForHistory.execute())
        }
    }

}