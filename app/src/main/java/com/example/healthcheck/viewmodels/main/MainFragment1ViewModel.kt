package com.example.healthcheck.viewmodels.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.usecase.GetHeartForDayFromDb
import com.example.domain.usecase.GetHoursOfSleepForDayFromDb
import com.example.domain.usecase.GetWeightForDayFromDb
import com.example.domain.usecase.steps.GetCountOfStepsForDayFromDb
import com.example.domain.usecase.steps.GetLastStepsIdAndDate
import com.example.domain.usecase.steps.GetStepsTarget
import com.example.domain.usecase.weight.GetWeightTarget
import com.example.healthcheck.App
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar

class MainFragment1ViewModel : ViewModel() {

    private val _daySteps = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val daySteps = _daySteps.asSharedFlow()

    private val _daySleep = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val daySleep = _daySleep.asSharedFlow()

    private val _dayWeight = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val dayWeight = _dayWeight.asSharedFlow()

    private val _dayHeart = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val dayHeart = _dayHeart.asSharedFlow()

    private val _stepsTarget = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val stepsTarget = _stepsTarget.asSharedFlow()

    private val _weightTarget = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val weightTarget = _weightTarget.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getLastStepsIdAndDate = GetLastStepsIdAndDate(repository = Repositories.stepsRepository)
            getLastStepsIdAndDate.execute().collect { stepsEntity ->
                val currentDate = SimpleDateFormat("dd.MM").format(Calendar.getInstance().timeInMillis)
                val lastDate = SimpleDateFormat("dd.MM").format(stepsEntity.date)
                if (lastDate == currentDate) {
                    val getCountOfStepsForDayFromDb = GetCountOfStepsForDayFromDb(repository = Repositories.stepsRepository)
                    getCountOfStepsForDayFromDb.execute().collect { countOfSteps ->
                        _daySteps.emit(countOfSteps)
                    }

                }
                else {
                    _daySteps.emit(0)
                }
            }
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getHoursOfSleepForDayFromDb = GetHoursOfSleepForDayFromDb(repository = Repositories.sleepRepository)
            //TODO переписать эту часть кода
            val lastDate = withContext(AppDispatchers.io) {
                try {
                    return@withContext SimpleDateFormat("dd.MM").format(Repositories.sleepRepository.getLastDate().date)
                }
                catch (e : NullPointerException) {
                    Log.d("Exceptions", ": ${e.localizedMessage}")
                }

            }
            val currentDate = SimpleDateFormat("dd.MM").format(Calendar.getInstance().timeInMillis)
            if (lastDate != currentDate) {
                _daySleep.emit("")
            }
            else {
                _daySleep.emit(getHoursOfSleepForDayFromDb.execute())
            }

        }

        viewModelScope.launch(AppDispatchers.main) {
            val getWeightForDayFromDb = GetWeightForDayFromDb(repository = Repositories.weightRepository)
            val lastDate = withContext(AppDispatchers.io) {
                try {
                    return@withContext SimpleDateFormat("dd.MM").format(Repositories.weightRepository.getWeightForDay().date)
                }
                catch (e : NullPointerException) {
                    Log.d("Exceptions", ": ${e.localizedMessage}")
                }

            }
            val currentDate = SimpleDateFormat("dd.MM").format(Calendar.getInstance().timeInMillis)
            if (lastDate != currentDate) {
                _dayWeight.emit(0F)
            }
            else {
                _dayWeight.emit(getWeightForDayFromDb.execute())
            }

        }

        viewModelScope.launch(AppDispatchers.main) {
            val getHeartForDayFromDb = GetHeartForDayFromDb(repository = Repositories.heartRepository)
            val lastDate = withContext(AppDispatchers.io) {
                try {
                    return@withContext SimpleDateFormat("dd.MM").format(Repositories.heartRepository.getCardioForDay().date)
                }
                catch (e : NullPointerException) {
                    Log.d("Exceptions", ": ${e.localizedMessage}")
                }
            }
            val currentDate = SimpleDateFormat("dd.MM").format(Calendar.getInstance().timeInMillis)
            if (lastDate != currentDate) {
                _dayHeart.emit("")
            }
            else {
                _dayHeart.emit(getHeartForDayFromDb.execute())
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