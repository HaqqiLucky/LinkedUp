package com.example.linkedup.item
import com.example.linkedup.item.Experience
import android.media.RouteListingPreference.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.ItemExperiencesBinding

class ExperienceAdapter (private val itemList: List<Experience>) : RecyclerView.Adapter<ExperienceAdapter.ItemViewHolder>(){


    class ItemViewHolder(val binding: ItemExperiencesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemExperiencesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val iniItem = itemList[position]
        holder.binding.judul.text = iniItem.judul
        holder.binding.deskExpe.text = iniItem.deskExpe
    }

    override fun getItemCount(): Int = itemList.size

    }

