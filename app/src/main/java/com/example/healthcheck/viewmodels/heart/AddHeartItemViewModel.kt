package com.example.healthcheck.viewmodels.heart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.models.Heart
import com.example.domain.usecase.heart.InsertHeartItem
import com.example.healthcheck.models.HeartItemParams
import kotlinx.coroutines.launch
import java.util.Calendar

class AddHeartItemViewModel : ViewModel() {
    fun insertHeartItem(params : HeartItemParams) {
        viewModelScope.launch {
            val insertHeartItem = InsertHeartItem(repository = Repositories.heartRepository)
            insertHeartItem.execute(
                Heart(
                    id = 0,
                    pressureUp = params.pressureUp,
                    pressureDown = params.pressureDown,
                    pulse = params.pulse,
                    date = Calendar.getInstance().timeInMillis
                )
            )
        }
    }

}