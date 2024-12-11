package com.example.linkedup.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.ItemEducationBinding
import com.example.linkedup.utils.Education

class EducationAdapter(
    private val educations: List<Education>,
    private val onEditClick: (Education) -> Unit,
    private val onDeleteClick: (Education) -> Unit
) : RecyclerView.Adapter<EducationAdapter.EducationViewHolder>() {

    inner class EducationViewHolder(private val binding: ItemEducationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(education: Education) {
            binding.degree.text = education.degree
            
            binding.tombolEdit.setOnClickListener {
                onEditClick(education)
            }
            
            binding.tombolDelete.setOnClickListener {
                onDeleteClick(education)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        val binding = ItemEducationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EducationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        holder.bind(educations[position])
    }

    override fun getItemCount() = educations.size
} 