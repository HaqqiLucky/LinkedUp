package com.example.linkedup.item

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.linkedup.R
import com.example.linkedup.databinding.ItemCompany2Binding
import com.example.linkedup.databinding.ItemLokerBinding
import com.example.linkedup.fetch.ConfigManager
import com.example.linkedup.fetch.Job
import com.example.linkedup.fetch.Company
import com.example.linkedup.fetch.JobResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeAdapter(
    private val showDeleteConfirmationDialog: (id: String) -> Unit,
    private val navigateToEditLokerPostFragment: (id: String, title: String, deskripsi: String, gaji: Int, company: String) -> Unit,
    private val detail: (id: String, title: String, gaji: String, deskripsi: String, waktu: String, company: String, image: String) -> Unit,
) : ListAdapter<Any, RecyclerView.ViewHolder>(HomeDiffCallback()) {

    companion object {
        const val LOKER_ITEM = 0
        const val COMPANY_ITEM = 1
    }

    class LokerViewHolder private constructor(
        val binding: ItemLokerBinding,
        private val showDeleteConfirmationDialog: (id:String) -> Unit,
        private val navigateToEditLokerPostFragment: (id: String, title: String, deskripsi: String, gaji: Int, company: String) -> Unit,
        private val detail: (id: String, title: String, gaji: String, deskripsi: String, waktu: String, company: String, image: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: JobResponse) {
            val maxLength = 20
            val originalText = item.title
            val truncatedText = if (originalText.length > maxLength) {
                originalText.substring(0, maxLength) + " ..."
            } else {
                originalText
            }
            binding.title.text = truncatedText
            binding.gaji.text = "Rp. ${item.salary}"
            binding.company.text = item.company?.name
            val formattedDate = formatDate(item.createdAt)
            binding.waktu.text = formattedDate
            val BASE_URL = ConfigManager.getBaseUrl()
            val imageUrl = "${BASE_URL}${item.image}"
            Glide.with(itemView.context)
                .load(imageUrl)
                .error(R.drawable.headtest)
                .into(binding.jobImage)

            if (item.salary >= 7000000) {
                binding.gaji.setTextColor(Color.rgb(0,255,70))
            } else if (item.salary <= 2000000) {
                binding.gaji.setTextColor(Color.MAGENTA)
            }

            binding.hapus.setOnClickListener {
                showDeleteConfirmationDialog(item._id)
            }
            binding.edit.setOnClickListener {
                item.company?.let { it2 -> navigateToEditLokerPostFragment(item._id, item.title, item.description, item.salary, it2.name) }
            }
            binding.title.setOnClickListener {
                item.company?.let { it1 -> detail(item._id, item.title, item.salary.toString(), item.description, formattedDate, it1.name, item.image) }
            }
        }
        fun formatDate(date: Date): String {
            val outputFormat = SimpleDateFormat("EEE, dd/MM/yyyy HH:mm", Locale.getDefault())
            return outputFormat.format(date)
        }

        companion object {
            fun from(
                parent: ViewGroup,
                showDeleteConfirmationDialog: (id: String) -> Unit,
                navigateToEditLokerPostFragment: (id: String, title: String, deskripsi: String, gaji: Int, company: String) -> Unit,
                detail: (id: String, title: String, gaji: String, deskripsi: String, waktu: String, company: String, image: String) -> Unit
            ): LokerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLokerBinding.inflate(layoutInflater, parent, false)
                return LokerViewHolder(binding, showDeleteConfirmationDialog, navigateToEditLokerPostFragment, detail)
            }
        }
    }

    class CompanyViewHolder(private val binding: ItemCompany2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(company: Company) {
            binding.nama.text = company.name
            binding.alamat.text = company.address
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
            is CompanyViewHolder -> {
                val company = getItem(position) as? Company // Pastikan data valid
                company?.let {
                    holder.bind(it)
                }
            }
            is LokerViewHolder -> {
                val loker = getItem(position) as? JobResponse // Safe cast
                loker?.let {
                    holder.bind(it)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is JobResponse -> LOKER_ITEM
            else -> COMPANY_ITEM
        }
    }

    class HomeDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Job && newItem is Job -> oldItem._id == newItem._id
                oldItem is Company && newItem is Company -> oldItem._id == newItem._id
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Job && newItem is Job -> oldItem == newItem
                oldItem is Company && newItem is Company -> oldItem == newItem
                else -> false
            }
        }
    }
}
