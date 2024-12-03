package com.example.linkedup.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.R
import com.example.linkedup.fetch.Company
import com.example.linkedup.fetch.CompanyResponse

class CompanyAdapter(private val companyList: List<CompanyResponse>, private val pindahEdit: (id: String, nama: String, alamat: String, web: String) -> Unit, private val showDeleteConfirmationDialog: (id: String) -> Unit) : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {
    class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTextView: TextView = itemView.findViewById(R.id.nama)
        val alamatTextView: TextView = itemView.findViewById(R.id.alamat)
        val btnedit: ImageButton = itemView.findViewById(R.id.tombolEditTabelUser)
        val btndel: ImageButton = itemView.findViewById(R.id.tombolHapusTabelUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_company, parent, false)
        return CompanyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val current = companyList[position]
        holder.namaTextView.text = current.name
        holder.alamatTextView.text = current.address

        holder.btnedit.setOnClickListener {
            pindahEdit(current._id, current.name, current.address, current.website.toString())
        }
        holder.btndel.setOnClickListener {
            showDeleteConfirmationDialog(current._id)
        }
    }

    override fun getItemCount() = companyList.size
}