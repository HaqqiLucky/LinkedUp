package com.example.linkedup.utils

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    val name: String,
    val alamat: String,
    @ColumnInfo (name = "no_tlpn") val telepon : String,
    val pengalaman: String,
    @ColumnInfo (name = "gender") val jenis_kelamin : String,
    @ColumnInfo (name = "edukasi") val riwayat_edukasi: String,
    @ColumnInfo (name = "domisili") val alamat_saat_ini: String,
    @ColumnInfo (name = "isAdmin") val isAdmin : Boolean,
    val image: String

)