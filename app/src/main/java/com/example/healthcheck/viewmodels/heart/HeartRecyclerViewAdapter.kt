package com.example.healthcheck.viewmodels.heart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Heart
import com.example.healthcheck.R
import com.example.healthcheck.databinding.HeartItemBinding
import java.text.SimpleDateFormat
import java.util.Locale


class HeartRecyclerViewAdapter(
    private val heartActionListener: HeartActionListener
) : RecyclerView.Adapter<HeartRecyclerViewAdapter.HeartViewHolder>(), View.OnClickListener {

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

        binding.itemBox.setOnClickListener(this)

        return HeartViewHolder(binding)
    }

    override fun getItemCount(): Int = cardioList.size

    override fun onBindViewHolder(holder: HeartViewHolder, position: Int) {
        val heart = cardioList[position]

        with(holder.binding) {
            itemBox.tag = heart
        }

        with(holder.binding) {
            upPressureText.text = heart.pressureUp.toString()
            downPressureText.text = heart.pressureDown.toString()
            pulseText.text = heart.pulse.toString()
            dateText.hint = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(heart.date)
        }
    }

    override fun onClick(view: View) {
        val heart = view.tag as Heart
        when(view.id) {

            R.id.itemBox -> {
                heartActionListener.onBoxClickAction(heart = heart)
            }

        }
    }

}

interface HeartActionListener {

    fun onBoxClickAction(heart : Heart)

}