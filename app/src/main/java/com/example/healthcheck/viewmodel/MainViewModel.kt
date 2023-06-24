package com.example.healthcheck.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.view.mainFragment1
import com.example.healthcheck.view.mainFragment2
import com.example.healthcheck.view.mainFragment3
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel() : ViewModel() {

    //lateinit var currentFrag : MutableLiveData<Int>

    val fragList = listOf(
        mainFragment1.newInstance(),
        mainFragment2.newInstance(),
        mainFragment3.newInstance(),
    )

    init {
        //currentFrag.value = 0
    }

    fun getAllMedicines() : LiveData<List<Medicines>> {
        return Repositories.medicinesRepository.getAllMedicines()
    }

}