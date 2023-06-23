package com.example.healthcheck.util.animations

import android.animation.ObjectAnimator
import android.os.Handler
import android.widget.ProgressBar
import androidx.core.os.HandlerCompat


object ProgressBarAnimation {

    fun animateProgressBar(progressBar: ProgressBar, maxValue : Int) {
        val progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, maxValue)
        progressAnimator.setDuration(1000)
        progressAnimator.start()
    }

}