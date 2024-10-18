package com.example.linkedup.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.R
import com.example.linkedup.utils.Company

class CompanyAdapter(private val companyList: List<Company>) : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {
    class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTextView: TextView = itemView.findViewById(R.id.nama)
        val alamatTextView: TextView = itemView.findViewById(R.id.alamat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_company, parent, false)
        return CompanyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val current = companyList[position]
        holder.namaTextView.text = current.nama
        holder.alamatTextView.text = current.alamat
    }

    override fun getItemCount() = companyList.size
}