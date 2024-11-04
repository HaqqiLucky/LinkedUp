package com.example.linkedup.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.ItemLokerBinding
import com.example.linkedup.utils.Loker

class LokerAdapter(
    private val showDeleteConfirmationDialog: (Context, Loker) -> Unit,
    private val navigateToEditLokerPostFragment: (id: Int, title: String, deskripsi: String, gaji: Int, company: String) -> Unit,
    private val detail: (title: String, gaji: String, deskripsi: String, waktu: String, company: String) -> Unit
) : ListAdapter<Loker, LokerAdapter.LokerViewHolder>(LokerDiffCallback()) {

    class LokerViewHolder private constructor(
        val binding: ItemLokerBinding,
        private val showDeleteConfirmationDialog: (Context, Loker) -> Unit,
        private val navigateToEditLokerPostFragment: (id: Int, title: String, deskripsi: String, gaji: Int, company: String) -> Unit,
        private val detail: (title: String, gaji: String, deskripsi: String, waktu: String, company: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Loker) {
            binding.title.text = item.title
            binding.gaji.text = "Rp. ${item.gaji}"
            binding.company.text = item.instansi
            binding.waktu.text = item.dibuat

            binding.hapus.setOnClickListener {
                showDeleteConfirmationDialog(binding.root.context, item)
            }
            binding.edit.setOnClickListener {
                navigateToEditLokerPostFragment(item._id, item.title, item.deskripsi, item.gaji, item.instansi)
            }
            binding.title.setOnClickListener {
                detail(item.title, item.gaji.toString(), item.deskripsi, item.dibuat, item.instansi)
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                showDeleteConfirmationDialog: (Context, Loker) -> Unit,
                navigateToEditLokerPostFragment: (id: Int, title: String, deskripsi: String, gaji: Int, company: String) -> Unit,
                detail: (title: String, gaji: String, deskripsi: String, waktu: String, company: String) -> Unit
            ): LokerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLokerBinding.inflate(layoutInflater, parent, false)
                return LokerViewHolder(binding, showDeleteConfirmationDialog, navigateToEditLokerPostFragment, detail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LokerViewHolder {
        return LokerViewHolder.from(parent, showDeleteConfirmationDialog, navigateToEditLokerPostFragment, detail)
    }

    override fun onBindViewHolder(holder: LokerViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class LokerDiffCallback : DiffUtil.ItemCallback<Loker>() {
        override fun areItemsTheSame(oldItem: Loker, newItem: Loker): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Loker, newItem: Loker): Boolean {
            return oldItem == newItem
        }
    }
}
