package com.example.healthcheck.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.usecase.profile.SetGender
import com.example.domain.usecase.profile.SetUserName
import kotlinx.coroutines.launch

class StartFragment4ViewModel : ViewModel() {

    fun setName(name : String) {
        viewModelScope.launch(AppDispatchers.main) {
            val setUserName = SetUserName(repository = Repositories.profileStorage)
            setUserName.execute(name = name)
        }
    }

    fun setGender(gender : Boolean) {
        viewModelScope.launch(AppDispatchers.main) {
            val setGender = SetGender(repository = Repositories.profileStorage)
            setGender.execute(gender = gender)
        }
    }

}