package com.example.healthcheck.viewmodels.start

import androidx.lifecycle.ViewModel
import com.example.healthcheck.ui.start.StartFragment1
import com.example.healthcheck.ui.start.StartFragment2
import com.example.healthcheck.ui.start.StartFragment3

class StartViewModel : ViewModel() {

    val fragList = listOf(
        StartFragment1.newInstance(),
        StartFragment2.newInstance(),
        StartFragment3.newInstance(),
    )
}
