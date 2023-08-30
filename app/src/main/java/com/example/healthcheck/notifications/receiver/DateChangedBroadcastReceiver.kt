package com.example.healthcheck.notifications.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.Calendar

abstract class DateChangedBroadcastReceiver : BroadcastReceiver() {
    private var curDate = Calendar.getInstance()

    //author - android developer
    //https://stackoverflow.com/questions/5481386/date-and-time-change-listener-in-android

    /**
     * Called when the receiver detects that the time has changed. You should still check it yourself,
     * because you might already be synced with the new time
     */
    abstract fun onTimeChanged(previousDate: Calendar, newDate: Calendar)

    companion object {
        fun toString(cal: Calendar): String {
            return "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH)}-${cal.get(Calendar.DAY_OF_MONTH)}"
        }

        fun resetDate(date: Calendar) {
            date.set(Calendar.SECOND, 0)
            date.set(Calendar.MILLISECOND, 0)
        }

        fun areOfSameDate(date: Calendar, otherDate: Calendar) =
            date.get(Calendar.YEAR) == otherDate.get(Calendar.YEAR)
                    && date.get(Calendar.DAY_OF_YEAR) == otherDate.get(Calendar.DAY_OF_YEAR)
                    && date.get(Calendar.HOUR_OF_DAY) == otherDate.get(Calendar.HOUR_OF_DAY)
                    && date.get(Calendar.MINUTE) == otherDate.get(Calendar.MINUTE)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun register(context: Context, date: Calendar) {
        curDate = date.clone() as Calendar
        resetDate(curDate)
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK)
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
        context.registerReceiver(this, filter)
        val newDate = Calendar.getInstance()
        resetDate(newDate)
        if (!areOfSameDate(newDate, curDate)) {
            val previousDate = curDate.clone() as Calendar
            curDate = newDate
            onTimeChanged(previousDate, curDate)
        }
    }

    fun registerOnResume(activity: AppCompatActivity, date: Calendar, fragment: Fragment? = null) {
        register(activity as Context, date)
        val lifecycle = fragment?.lifecycle ?: activity.lifecycle
        lifecycle.addObserver(object : LifecycleObserver {
            @Suppress("unused")
            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                lifecycle.removeObserver(this)
                activity.unregisterReceiver(this@DateChangedBroadcastReceiver)
            }
        })
    }

    override fun onReceive(context: Context, intent: Intent) {
        val newDate = Calendar.getInstance()
        resetDate(newDate)
        if (!areOfSameDate(newDate, curDate)) {
            val previousDate = curDate.clone() as Calendar
            curDate = newDate
            onTimeChanged(previousDate, newDate)
        }
    }
}