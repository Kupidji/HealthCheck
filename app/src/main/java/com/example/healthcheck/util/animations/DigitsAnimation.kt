package com.example.healthcheck.util.animations

import android.animation.ValueAnimator
import android.widget.TextView
import java.util.Locale

object DigitsAnimation {

    fun digitsIntAnimation(textView : TextView, value : Int) {
        val animator = ValueAnimator.ofInt(0, value)
        animator.duration = 250
        animator.addUpdateListener { animation -> textView.text = animation.animatedValue.toString() }
        animator.start()
    }

    fun digitsFloatAnimation(textView : TextView, value : Float) {
        val animator = ValueAnimator.ofFloat(0F, value)
        animator.duration = 250
        animator.addUpdateListener { animation -> textView.text = String.format(Locale.US,"%.1f", animation.animatedValue) }
        animator.start()
    }

    fun digitsFloatAnimation(textView : TextView, value : Float, symbol : String) {
        val animator = ValueAnimator.ofFloat(0F, value)
        animator.duration = 250
        animator.addUpdateListener { animation -> textView.text = String.format(Locale.US,"%.1f", animation.animatedValue) + symbol }
        animator.start()
    }

}