package com.example.linkedup.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.ItemExperiencesBinding
import com.example.linkedup.databinding.ItemExperiencesHighlightedBinding
import com.example.linkedup.utils.Experience

class ExperienceAdapter(private val experiences: List<Experience>) : 
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val NORMAL_VIEW = 0
    private val HIGHLIGHTED_VIEW = 1

    override fun getItemViewType(position: Int): Int {
        return if (experiences[position].highligted) HIGHLIGHTED_VIEW else NORMAL_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HIGHLIGHTED_VIEW -> {
                val binding = ItemExperiencesHighlightedBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HighlightedViewHolder(binding)
            }
            else -> {
                val binding = ItemExperiencesBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                NormalViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val experience = experiences[position]
        when (holder) {
            is HighlightedViewHolder -> {
                holder.bind(experience)
            }
            is NormalViewHolder -> {
                holder.bind(experience)
            }
        }
    }

    override fun getItemCount() = experiences.size

    class NormalViewHolder(private val binding: ItemExperiencesBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        fun bind(experience: Experience) {
            binding.judul.text = experience.judulExperience
            binding.tempatExpe.text = experience.tempatExperience
        }
    }

    class HighlightedViewHolder(private val binding: ItemExperiencesHighlightedBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        fun bind(experience: Experience) {
            binding.judul.text = experience.judulExperience
            binding.tempatExpe.text = experience.tempatExperience
            binding.highlighted.text = "Highlighted"
        }
    }
}

