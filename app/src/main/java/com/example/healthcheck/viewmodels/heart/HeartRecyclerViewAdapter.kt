package com.example.healthcheck.viewmodels.heart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Heart
import com.example.healthcheck.databinding.HeartItemBinding
import java.text.SimpleDateFormat
import java.util.Locale


class HeartRecyclerViewAdapter() : RecyclerView.Adapter<HeartRecyclerViewAdapter.HeartViewHolder>() {

    var cardioList : List<Heart> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class HeartViewHolder(
        val binding : HeartItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HeartItemBinding.inflate(inflater, parent, false)

        //binding.root.setOnClickListener(this)

        return HeartViewHolder(binding)
    }

    override fun getItemCount(): Int = cardioList.size

    override fun onBindViewHolder(holder: HeartViewHolder, position: Int) {
        val heart = cardioList[position]
        with(holder.binding) {
            upPressureText.text = heart.pressureUp.toString()
            downPressureText.text = heart.pressureDown.toString()
            pulseText.text = heart.pulse.toString()
            dateText.hint = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(heart.date)
        }
    }

}