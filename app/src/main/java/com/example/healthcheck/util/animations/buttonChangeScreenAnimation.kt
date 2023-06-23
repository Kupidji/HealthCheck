package com.example.healthcheck.util.animations

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

object buttonChangeScreenAnimation {

    fun buttonChangeScreenAnimation(view : View, nav : NavController, d : NavDirections, o : NavOptions,navigation:(NavController, NavDirections, NavOptions) -> Unit) {
        view.animate()
            .setDuration(75)
            .scaleX(0.95f)
            .scaleY(0.95f)
            .withEndAction {
                navigation(nav, d, o)
            }
    }

}