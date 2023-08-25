package com.example.healthcheck.viewmodels.heart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Heart
import com.example.domain.usecase.heart.GetListOfHeartForLastNote
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HeartViewModel : ViewModel() {

//    private val _upperPressure = MutableSharedFlow<Int>(1, 0 ,BufferOverflow.DROP_OLDEST)
//    val upperPressure = _upperPressure.asSharedFlow()
//
//    private val _lowerPressure = MutableSharedFlow<Int>(1, 0 ,BufferOverflow.DROP_OLDEST)
//    val lowerPressure = _lowerPressure.asSharedFlow()
//
//    private val _pulse = MutableSharedFlow<Int>(1, 0 ,BufferOverflow.DROP_OLDEST)
//    val pulse = _pulse.asSharedFlow()
//
//    private val _lastId = MutableSharedFlow<Int>(1, 0 ,BufferOverflow.DROP_OLDEST)
//    val lastId = _lastId.asSharedFlow()
//
//    private val _lastDate = MutableSharedFlow<Long>(1, 0 ,BufferOverflow.DROP_OLDEST)
//    val lastDate = _lastDate.asSharedFlow()

    private val _listOfHeart = MutableSharedFlow<List<Heart>>(1, 0, BufferOverflow.DROP_OLDEST)
    val listOfHeart = _listOfHeart.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getListOfHeartForLastNote = GetListOfHeartForLastNote(repository = Repositories.heartRepository)
            _listOfHeart.emit(getListOfHeartForLastNote.execute())
        }

    }

//    suspend fun getCardioFromDataUpPressure(): Int {
//        val result = viewModelScope.async(Dispatchers.IO) {
//            var list = com.example.data.Repositories.heartRepository.getCardioForDay()
//            val sumResult = withContext(Dispatchers.Default) {
//                var sum = 0
//                for (heart in list) {
//                    sum = heart.pressureUp
//                }
//
//                return@withContext sum
//            }
//
//            return@async sumResult
//        }
//
//        return result.await()
//    }

//    suspend fun getCardioFromDataDownPressure(): Int {
//        val result = viewModelScope.async(Dispatchers.IO) {
//            var list = com.example.data.Repositories.heartRepository.getCardioForDay()
//            val sumResult = withContext(Dispatchers.Default) {
//                var sum = 0
//                for (heart in list) {
//                    sum = heart.pressureDown
//                }
//                return@withContext sum
//            }
//            return@async sumResult
//        }
//        return result.await()
//    }
//
//    suspend fun getCardioFromDataPulse(): Int = viewModelScope.async(Dispatchers.Default) {
//        val list = withContext(Dispatchers.IO) {
//            return@withContext com.example.data.Repositories.heartRepository.getCardioForDay()
//        }
//        var sum = 0
//        for (heart in list) {
//            sum = heart.pulse
//        }
//        return@async sum
//    }.await()
//
//    suspend fun getLastIdFromData(): Int = viewModelScope.async(Dispatchers.Default) {
//        val list = withContext(Dispatchers.IO) {
//            return@withContext com.example.data.Repositories.heartRepository.getCardioForDay()
//        }
//        var id = 0
//        for (heart in list) {
//            id = heart.id
//        }
//        return@async id
//    }.await()
//
//    suspend fun getLastDateFromData(): Long = viewModelScope.async(AppDispatchers.default) {
//        val list = withContext(AppDispatchers.io) {
//            return@withContext com.example.data.Repositories.heartRepository.getCardioForDay()
//        }
//        var date = 0L
//        for (heart in list) {
//            date = heart.date
//        }
//
//        return@async date
//    }.await()
//
//    fun insertHeart(heart : Heart) {
//        viewModelScope.launch {
//            com.example.data.Repositories.heartRepository.insertCardio(heart)
//        }
//    }
//
//    fun updateHeart(heart: Heart) {
//        viewModelScope.launch {
//            com.example.data.Repositories.heartRepository.updateCardio(heart)
//        }
//    }
//
//    fun setCurrentUpperPressure() {
//        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
//            _upperPressure.emit(getCardioFromDataUpPressure())
//        }
//    }
//
//    fun setCurrentLowerPressure() {
//        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
//            _lowerPressure.emit(getCardioFromDataDownPressure())
//        }
//    }
//
//    fun setCurrentPulse() {
//        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
//            _pulse.emit(getCardioFromDataPulse())
//        }
//    }
//
//    fun setCurrentId() {
//        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
//            _lastId.emit(getLastIdFromData())
//        }
//    }
//
//    fun setCurrentDate() {
//        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
//            _lastDate.emit(getLastDateFromData())
//        }
//    }


}