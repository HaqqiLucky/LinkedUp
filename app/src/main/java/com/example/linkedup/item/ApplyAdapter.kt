package com.example.linkedup.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.R
import com.example.linkedup.fetch.JobUsers

class ApplyAdapter(
    private val jobUsers: List<JobUsers>,
    private val applyJobs: (jobId: String, userId: String) -> Unit
) : RecyclerView.Adapter<ApplyAdapter.JobUsersViewHolder>() {

    class JobUsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTextView: TextView = itemView.findViewById(R.id.name)
        val deskripsiTextView: TextView = itemView.findViewById(R.id.deskripsi)
        val portfolioTextView: TextView = itemView.findViewById(R.id.portfolio)
        val companyTextView: TextView = itemView.findViewById(R.id.company)
        val btnApplyView: Button = itemView.findViewById(R.id.applyButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobUsersViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_apply, parent, false)
        return JobUsersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JobUsersViewHolder, position: Int) {
        val current = jobUsers[position]
        holder.namaTextView.text = current.user.name
        holder.companyTextView.text = current.job.title
        holder.deskripsiTextView.text = current.description
        holder.portfolioTextView.text = current.portfolioLink


        holder.btnApplyView.setOnClickListener {
            current.job._id?.let { it1 -> current.user._id?.let { it2 -> applyJobs(it1, it2) } }
        }
    }

    override fun getItemCount() = jobUsers.size
}