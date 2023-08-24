package com.example.healthcheck.util.animations

import android.view.View

object ButtonPress {

    fun buttonPressAnimation(view : View) {
        view.animate()
            .setDuration(75)
            .scaleX(0.95f)
            .scaleY(0.95f)
            .withEndAction {
                view.animate()
                    .setDuration(75)
                    .scaleX(1f)
                    .scaleY(1f)
            }
    }
}