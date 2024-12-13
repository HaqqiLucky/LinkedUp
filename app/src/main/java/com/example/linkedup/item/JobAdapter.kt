package com.example.linkedup.item

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.linkedup.R
import com.example.linkedup.databinding.ItemLokerBinding
import com.example.linkedup.fetch.ConfigManager
import com.example.linkedup.utils.JobEntity

class JobAdapter(
    private val detail: (id: String, title: String, gaji: String, deskripsi: String, waktu: String, company: String, image: String) -> Unit,
    private val showDeleteConfirmationDialog: (test: JobEntity) -> Unit,
    ) : ListAdapter<JobEntity, JobAdapter.JobViewHolder>(JobDiffCallback()) {

    // Meng-handle item pekerjaan yang ditampilkan di RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemLokerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding, detail, showDeleteConfirmationDialog)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)
        holder.bind(job)
    }

    // ViewHolder untuk item pekerjaan
    class JobViewHolder(
        private val binding: ItemLokerBinding,
        private val detail: (id: String, title: String, gaji: String, deskripsi: String, waktu: String, company: String, image: String) -> Unit,
        private val showDeleteConfirmationDialog: (test: JobEntity) -> Unit,
        ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: JobEntity) {
            val maxLength = 20
            val originalText = item.title
            val truncatedText = if (originalText.length > maxLength) {
                originalText.substring(0, maxLength) + " ..."
            } else {
                originalText
            }
            binding.title.text = truncatedText
            binding.gaji.text = "Rp. ${item.salary}"
            binding.company.text = item.company
            binding.waktu.text = item.createdAt

            if (item.salary >= 7000000) {
                binding.gaji.setTextColor(Color.rgb(0,255,70))
            } else if (item.salary <= 2000000) {
                binding.gaji.setTextColor(Color.MAGENTA)
            }

            binding.title.setOnClickListener {
                detail(item.id, item.title, item.salary.toString(), item.description, item.createdAt, item.company, "")
            }

            binding.hapus.setOnClickListener {
                showDeleteConfirmationDialog(item)
            }

            binding.hapus.visibility = View.VISIBLE
            binding.edit.visibility = View.GONE
        }
    }

    // Callback untuk perbandingan item
    class JobDiffCallback : DiffUtil.ItemCallback<JobEntity>() {
        override fun areItemsTheSame(oldItem: JobEntity, newItem: JobEntity): Boolean {
            return oldItem.id == newItem.id  // Bandingkan berdasarkan ID pekerjaan
        }

        override fun areContentsTheSame(oldItem: JobEntity, newItem: JobEntity): Boolean {
            return oldItem == newItem  // Bandingkan konten pekerjaan
        }
    }
}