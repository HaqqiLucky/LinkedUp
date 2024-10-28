package com.example.linkedup.item

import android.content.Context
import android.media.RouteListingPreference.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.linkedup.R
import com.example.linkedup.utils.User
import java.io.File

//pengalaman: String

class UserAdapter(private val userList: List<User>, private val showDeleteConfirmationDialog: (Context, User) ->
Unit, private val navigateToEditLokerPostFragment: (id: Int, nama: String,email: String,password: String,
                                                    deskripsi: String, gender:String,gambar: String ) ->
Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val namaTextView: TextView = itemView.findViewById(R.id.namauser)
        var registEmailEditText: EditText = itemView.findViewById(R.id.etRegistEmail)
        val registPasswordEditText: EditText = itemView.findViewById(R.id.etRegistPassword)
        val deskripsiTextView:TextView = itemView.findViewById(R.id.desk)
        val genderTextView:TextView = itemView.findViewById(R.id.gender)
        val alamatTextView:TextView = itemView.findViewById(R.id.alamat)
//        val pengalamanTextView: TextView = itemView.findViewById(R.id.experienceteks)
        val gambarTextView:ImageView = itemView.findViewById(R.id.potoprofil)

        val edit: Button = itemView.findViewById(R.id.tombolEdit)

//        val hapus
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_profile, parent, false)
        return UserViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val current = userList[position]
        holder.namaTextView.text =  current.name
        holder.deskripsiTextView.text = current.deskripsi
        holder.genderTextView.text = current.jenis_kelamin
        holder.alamatTextView.text = current.alamat
        holder.registEmailEditText.setText(current.email)
        holder.registPasswordEditText.setText(current.password)

        val imagePath = current.image
        Glide.with(holder.gambarTextView.context)
            .load(File(imagePath))  // Mengambil gambar dari file lokal
            .placeholder(R.drawable.person1)  // Gambar placeholder jika tidak ada gambar
            .error(R.drawable.headtest)  // Gambar jika ada error saat memuat gambar
            .circleCrop()  // Mengubah gambar menjadi bentuk lingkaran
            .into(holder.gambarTextView)  // Memasukkan gambar ke ImageView
    }

    override fun getItemCount() = userList.size
}