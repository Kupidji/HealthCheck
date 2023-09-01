package com.example.healthcheck.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.usecase.GetAverageOfSleepForMonth
import com.example.domain.usecase.GetHeartForMonthFromDb
import com.example.domain.usecase.GetWeightForMonthFromDb
import com.example.domain.usecase.steps.GetAverageOfStepsForMonthFromDb
import com.example.domain.usecase.steps.GetStepsTarget
import com.example.domain.usecase.weight.GetWeightTarget
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainFragment3ViewModel : ViewModel() {
    private val _totalStepsForMonth = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalStepsForMonth = _totalStepsForMonth.asSharedFlow()

    private val _averageSleepMonth = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val averageSleepMonth = _averageSleepMonth.asSharedFlow()

    private val _totalWeightForMonth = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalWeightForMonth = _totalWeightForMonth.asSharedFlow()

    private val _monthHeart = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val monthHeart = _monthHeart.asSharedFlow()

    private val _stepsTarget = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val stepsTarget = _stepsTarget.asSharedFlow()

    private val _weightTarget = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val weightTarget = _weightTarget.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getAverageOfStepsForMonthFromDb = GetAverageOfStepsForMonthFromDb(repository = Repositories.stepsRepository)
            getAverageOfStepsForMonthFromDb.execute().collect { average ->
                _totalStepsForMonth.emit(average)
            }
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getAverageOfSleepForMonth = GetAverageOfSleepForMonth(repository = Repositories.sleepRepository)
            getAverageOfSleepForMonth.execute().collect { time ->
                _averageSleepMonth.emit(time)
            }
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getWeightForMonthFromDb = GetWeightForMonthFromDb(repository = Repositories.weightRepository)
            getWeightForMonthFromDb.execute().collect { weight ->
                _totalWeightForMonth.emit(weight)
            }
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getHeartForMonthFromDb = GetHeartForMonthFromDb(repository = Repositories.heartRepository)
            getHeartForMonthFromDb.execute().collect { heart ->
                _monthHeart.emit(heart)
            }
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getStepsTarget = GetStepsTarget(repository = Repositories.stepsStorage)
            _stepsTarget.emit(getStepsTarget.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getWeightTarget = GetWeightTarget(repository = Repositories.weightStorage)
            _weightTarget.emit(getWeightTarget.execute())
        }

    }

}