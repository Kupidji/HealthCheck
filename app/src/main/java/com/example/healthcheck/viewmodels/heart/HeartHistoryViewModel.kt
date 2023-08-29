package com.example.healthcheck.viewmodels.heart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Heart
import com.example.domain.usecase.heart.GetListOfHeartForHistory
import com.example.domain.usecase.heart.UpdateHeart
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HeartHistoryViewModel : ViewModel() {

    private val _historyHeartList = MutableSharedFlow<List<Heart>>(1, 0, BufferOverflow.DROP_OLDEST)
    val historyHeartList = _historyHeartList.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getListOfHeartForHistory = GetListOfHeartForHistory(repository = Repositories.heartRepository)
            getListOfHeartForHistory.execute().collect { list ->
                _historyHeartList.emit(list)
            }
        }

    }

    fun updateHeart(heart: Heart, upPressure : Int, downPressure : Int, pulse : Int) {
        viewModelScope.launch {
            val updateHeart = UpdateHeart(repository = Repositories.heartRepository)
            val ourHeart = Heart (
                id = heart.id,
                pressureUp = upPressure,
                pressureDown = downPressure,
                pulse = pulse,
                date = heart.date
            )
            updateHeart.execute(ourHeart)
        }
    }

}