package com.example.healthcheck.viewmodels.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.usecase.profile.SetAge
import kotlinx.coroutines.launch

class StartFragment5ViewModel : ViewModel() {

    fun setAge(age : Int) {
        viewModelScope.launch(AppDispatchers.main) {
            val setAge = SetAge(repository = Repositories.profileStorage)
            setAge.execute(age = age)
        }
    }

}