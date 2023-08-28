package com.example.healthcheck.viewmodels.sleep

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Sleep
import com.example.healthcheck.R
import com.example.healthcheck.databinding.SleepItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SleepHistoryRecyclerViewAdapter(
    private val sleepActionListener: SleepActionListener
) : RecyclerView.Adapter<SleepHistoryRecyclerViewAdapter.SleepViewHolder>(), View.OnClickListener {

    class SleepViewHolder(
        val binding : SleepItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var list = emptyList<Sleep>()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SleepItemBinding.inflate(inflater, parent, false)

        binding.itemBox.setOnClickListener(this)

        return SleepViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SleepViewHolder, position: Int) {
        val sleep = list[position]

        with(holder.binding) {
            itemBox.tag = sleep
        }

        with(holder.binding) {
            sleepText.text = convertTime(sleep.timeOfSleep)
            dateText.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(sleep.date)
        }
    }

    private fun convertTime(totalTime : Long) : String {
        val hours = Math.abs((totalTime) / 1000 / 60 / 60).toString()
        var minutes = Math.abs((totalTime) / 1000 / 60 % 60).toString()
        if (minutes.length % 2 != 0)
            minutes = "0$minutes"
        return "$hours:$minutes"
    }

    override fun onClick(view: View) {
        val sleep = view.tag as Sleep
        when(view.id) {

            R.id.itemBox -> {
                sleepActionListener.onBoxClickAction(sleep = sleep)
            }

        }
    }

}

interface SleepActionListener {

    fun onBoxClickAction(sleep : Sleep)

}