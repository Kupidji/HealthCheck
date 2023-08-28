package com.example.healthcheck.viewmodels.heart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Heart
import com.example.domain.usecase.heart.GetListOfHeartForHistory
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
            _historyHeartList.emit(getListOfHeartForHistory.execute())
        }

    }

}