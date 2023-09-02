package com.example.healthcheck.viewmodels.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.usecase.profile.SetHeight
import kotlinx.coroutines.launch

class StartFragment6ViewModel : ViewModel() {

    fun setHeight(height : Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val setHeight = SetHeight(repository = Repositories.profileStorage)
            setHeight.execute(height = height)
        }
    }

}