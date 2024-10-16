package com.example.linkedup.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.R
import com.example.linkedup.utils.Loker

// LokerAdapter.kt
class LokerAdapter : RecyclerView.Adapter<LokerAdapter.LokerViewHolder>() {

    private var lokerList = emptyList<Loker>()

    class LokerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val gajiTextView: TextView = itemView.findViewById(R.id.gaji)
        val instansiTextView: TextView = itemView.findViewById(R.id.company)
        val dibuatTextView: TextView = itemView.findViewById(R.id.waktu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LokerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loker, parent, false)
        return LokerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LokerViewHolder, position: Int) {
        val current = lokerList[position]
        holder.titleTextView.text = current.title
        holder.gajiTextView.text = "Rp. ${current.gaji}"
        holder.instansiTextView.text = "${current.instansi}"
        holder.dibuatTextView.text = "${current.dibuat}"
    }

    override fun getItemCount() = lokerList.size

    fun setLoker(lokers: List<Loker>) {
        this.lokerList = lokers
        notifyDataSetChanged()
    }
}

