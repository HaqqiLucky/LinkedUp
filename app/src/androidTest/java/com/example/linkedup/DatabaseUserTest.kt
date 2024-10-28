//package com.example.linkedup
//
//import android.content.Context
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.linkedup.utils.LokerDatabase
//import com.example.linkedup.utils.User
//import com.example.linkedup.utils.UserDao
//import kotlinx.coroutines.runBlocking
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import java.io.IOException
//
//@RunWith(AndroidJUnit4::class)
//class DatabaseLoker {
//    private lateinit var database: LokerDatabase
//    private lateinit var userDao: UserDao
//
//    @Before
//    fun setUp() {
//        val context: Context = ApplicationProvider.getApplicationContext()
//        database = Room.inMemoryDatabaseBuilder(context,
//            LokerDatabase::class.java
//        ).build()
//        userDao = database.userDao()
//    }
//
//    @After
//    @Throws(IOException::class)
//    fun dbClose() = database.close()
//
//    val Hai = listOf(
//        User(name = "Haqi", alamat = "Madiun", email = "haqi@kakak.com", password = "1", deskripsi = "Saya manusia", jenis_kelamin = "(He/Him)", isAdmin = true),
//        User(name = "Triani", alamat = "Madiun", email = "tri@kakak.com", password = "1", deskripsi = "Saya manusia", jenis_kelamin = "(She/Her)", isAdmin = true),
//        User(name = "Lopi", alamat = "Madiun", email = "lop@kakak.com", password = "1", deskripsi = "Saya manusia", jenis_kelamin = "(She/Her)", isAdmin = true),
//        User(name = "Muaz", alamat = "Madiun", email = "muz@kakak.com", password = "1", deskripsi = "Saya manusia", jenis_kelamin = "(He/Him)", isAdmin = true)
//    )
//
//    @Test
//    @Throws(Exception::class)
//    fun insertAndRetrieve() = runBlocking {
//        Hai.forEach { userDao.insert(it) }
//        val allUser = userDao.getAllUser()
//        assert(allUser.size == 4)
//        allUser.forEach { user ->
//            println("User: $user")
//        }
//    }
//
//}