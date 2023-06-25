package com.example.healthcheck.model.mainscreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.view.mainFragment1
import com.example.healthcheck.view.mainFragment2
import com.example.healthcheck.view.mainFragment3

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