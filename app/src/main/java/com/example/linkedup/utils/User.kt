package com.example.linkedup.utils

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    val name: String,
    val alamat: String? = null,
    val email: String,
    val password: String,
    val deskripsi : String? = null,
    @ColumnInfo (name = "gender") val jenis_kelamin : String? = null,
    val isAdmin : Boolean,
    val image: String? = null,
//    @ColumnInfo (name = "no_tlpn") val telepon : String,
//    val pengalaman: String,
//    @ColumnInfo (name = "edukasi") val riwayat_edukasi: String,
//    @ColumnInfo (name = "domisili") val alamat_saat_ini: String,
)