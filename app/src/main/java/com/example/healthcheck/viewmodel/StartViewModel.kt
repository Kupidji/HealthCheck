package com.example.healthcheck.viewmodel

import androidx.lifecycle.ViewModel
import com.example.healthcheck.view.StartFragment1
import com.example.healthcheck.view.StartFragment2
import com.example.healthcheck.view.StartFragment3

class StartViewModel : ViewModel() {

    val fragList = listOf(
        StartFragment1.newInstance(),
        StartFragment2.newInstance(),
        StartFragment3.newInstance(),
    )
}
