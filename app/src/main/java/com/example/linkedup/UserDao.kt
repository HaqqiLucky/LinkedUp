package com.example.linkedup

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll(): Array<User>

    @Query("SELECT * FROM users WHERE name = :name")
    fun getColorByName(name: String): LiveData<User>

    @Query("SELECT * FROM users WHERE hex_color = :hex")
    fun getColorByHex(hex: String): LiveData<User>

    @Insert
    fun insert(vararg user: User)

}
