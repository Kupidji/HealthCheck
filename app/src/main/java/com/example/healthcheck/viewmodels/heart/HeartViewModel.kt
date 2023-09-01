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

    private val _listOfHeart = MutableSharedFlow<List<Heart>>(1, 0, BufferOverflow.DROP_OLDEST)
    val listOfHeart = _listOfHeart.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getListOfHeartForLastNote = GetListOfHeartForLastNote(repository = Repositories.heartRepository)
            getListOfHeartForLastNote.execute().collect { list ->
                _listOfHeart.emit(list)
            }
        }

    }

}