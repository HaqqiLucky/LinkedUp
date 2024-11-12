package com.example.linkedup.item

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.ItemCompany2Binding
import com.example.linkedup.databinding.ItemLokerBinding
import com.example.linkedup.utils.Company
import com.example.linkedup.utils.Loker

class HomeAdapter(
    private val showDeleteConfirmationDialog: (Context, Loker) -> Unit,
    private val navigateToEditLokerPostFragment: (id: Int, title: String, deskripsi: String, gaji: Int, company: String) -> Unit,
    private val detail: (title: String, gaji: String, deskripsi: String, waktu: String, company: String) -> Unit,
) : ListAdapter<Any, RecyclerView.ViewHolder>(HomeDiffCallback()) {

    companion object {
        const val LOKER_ITEM = 0
        const val COMPANY_ITEM = 1
    }

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

            if (item.gaji >= 7000000) {
                binding.gaji.setTextColor(Color.GREEN)
            }

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

    class CompanyViewHolder(private val binding: ItemCompany2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(company: Company) {
            binding.nama.text = company.nama
            binding.alamat.text = company.alamat
        }

        companion object {
            fun from(parent: ViewGroup): CompanyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCompany2Binding.inflate(layoutInflater, parent, false)
                return CompanyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LOKER_ITEM -> LokerViewHolder.from(parent, showDeleteConfirmationDialog, navigateToEditLokerPostFragment, detail)
            else -> CompanyViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LokerViewHolder -> {
                val loker = getItem(position) as? Loker // Safe cast
                loker?.let {
                    holder.bind(it)
                }
            }
            is CompanyViewHolder -> {
                val company = getItem(position) as? Company // Pastikan data valid
                company?.let {
                    holder.bind(it)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Loker -> LOKER_ITEM
            else -> COMPANY_ITEM
        }
    }

    class HomeDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Loker && newItem is Loker -> oldItem._id == newItem._id
                oldItem is Company && newItem is Company -> oldItem._id == newItem._id
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Loker && newItem is Loker -> oldItem == newItem
                oldItem is Company && newItem is Company -> oldItem == newItem
                else -> false
            }
        }
    }
}
