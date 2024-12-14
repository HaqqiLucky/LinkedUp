package com.example.linkedup.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.ItemExperiencesBinding
import com.example.linkedup.databinding.ItemExperiencesHighlightedBinding
import com.example.linkedup.utils.Experience

class ExperienceAdapter(
    private val experiences: List<Experience>,
    private val onEditClick: (Experience) -> Unit,
    private val onDeleteClick: (Experience) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_NORMAL = 1
    private val VIEW_TYPE_HIGHLIGHTED = 2

    inner class NormalViewHolder(private val binding: ItemExperiencesBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        fun bind(experience: Experience) {
            binding.judul.text = experience.title
            binding.tempatExpe.text = experience.company
            
            binding.tombolEdit.setOnClickListener {
                onEditClick(experience)
            }
            
            binding.tombolDelete.setOnClickListener {
                onDeleteClick(experience)
            }
        }
    }

    inner class HighlightedViewHolder(private val binding: ItemExperiencesHighlightedBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        fun bind(experience: Experience) {
            binding.judul.text = experience.title
            binding.tempatExpe.text = experience.company
            binding.highlighted.text = "Highlighted"
            
            binding.tombolEdit.setOnClickListener {
                onEditClick(experience)
            }
            
            binding.tombolDelete.setOnClickListener {
                onDeleteClick(experience)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (experiences[position].isHighlighted) VIEW_TYPE_HIGHLIGHTED else VIEW_TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HIGHLIGHTED -> {
                val binding = ItemExperiencesHighlightedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HighlightedViewHolder(binding)
            }
            else -> {
                val binding = ItemExperiencesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                NormalViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HighlightedViewHolder -> holder.bind(experiences[position])
            is NormalViewHolder -> holder.bind(experiences[position])
        }
    }

    override fun getItemCount() = experiences.size
}

