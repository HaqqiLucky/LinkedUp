package com.example.linkedup.utils

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll(): Flow<User>

    @Query("SELECT * from users ORDER BY _id DESC")
    suspend fun getAllUser(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users: User)

    @Update
    suspend fun update(users: User)

    @Delete
    suspend fun delete(users: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?

//    @Query("SELECT * FROM users WHERE alamat = :alamat")
//    fun getUserByalamat(alamat: String): LiveData<User>
//
//    @Query("SELECT * FROM users WHERE no_tlpn = :telepon")
//    fun getUserByTelepon(telepon: String): LiveData<User>
//
//    @Query("SELECT * FROM users WHERE pengalaman = :pengalaman")
//    fun getUserByPengalaman(pengalaman: String): LiveData<User>
//
//    @Query ("SELECT * FROM users WHERE gender = :jenis_kelamin")
//    fun getUserByGender(jenis_kelamin : String): LiveData<User>
//
//    @Query ("SELECT * FROM users WHERE edukasi = :riwayat_edukasi")
//    fun getUserByEdukasi(riwayat_edukasi : String): LiveData<User>
//
//    @Query ("SELECT * FROM users WHERE domisili = :alamat_saat_ini")
//    fun getUserByDomisili(alamat_saat_ini : String): LiveData<User>




//    @Insert
//    fun insert(vararg user: User)

}
