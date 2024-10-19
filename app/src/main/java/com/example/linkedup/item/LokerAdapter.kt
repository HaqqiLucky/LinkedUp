package com.example.linkedup.item

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.R
import com.example.linkedup.utils.Loker
import kotlinx.coroutines.launch

// LokerAdapter.kt
class LokerAdapter(private val lokerList: List<Loker>, private val showDeleteConfirmationDialog: (Context, Loker) -> Unit, private val navigateToEditLokerPostFragment: (id: Int, title: String, deskripsi: String, gaji: Int, company: String) -> Unit) : RecyclerView.Adapter<LokerAdapter.LokerViewHolder>() {
    class LokerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val gajiTextView: TextView = itemView.findViewById(R.id.gaji)
        val instansiTextView: TextView = itemView.findViewById(R.id.company)
        val dibuatTextView: TextView = itemView.findViewById(R.id.waktu)

        val hapus: Button = itemView.findViewById(R.id.hapus)

        val edit: Button = itemView.findViewById(R.id.edit)
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

        holder.hapus.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, current)
        }
        holder.edit.setOnClickListener {
            navigateToEditLokerPostFragment(current._id, current.title, current.deskripsi, current.gaji, current.instansi)
        }
    }

    override fun getItemCount() = lokerList.size
}

