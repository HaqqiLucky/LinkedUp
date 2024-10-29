//package com.example.linkedup
//
//import android.content.Context
//import android.util.Log
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.linkedup.utils.Loker
//import com.example.linkedup.utils.LokerDao
//import com.example.linkedup.utils.LokerDatabase
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
//    private lateinit var lokerDao: LokerDao
//
//    @Before
//    fun setUp() {
//        val context: Context = ApplicationProvider.getApplicationContext()
//        database = Room.inMemoryDatabaseBuilder(context,
//            LokerDatabase::class.java
//        ).build()
//        lokerDao = database.lokerDao()
//    }
//
//    @After
//    @Throws(IOException::class)
//    fun dbClose() = database.close()
//
//    val loker = listOf(
//        Loker(
//        title = "Senior Software Engineer",
//        gaji = 10000000,
//        deskripsi = "Berpengalaman Menggunakan Android Studio",
//        instansi = "Tech Company",
//        dibuat = "2024-10-21",
//        status = true
//        ),
//        Loker(
//            title = "Junior Software Engineer",
//            gaji = 5000000,
//            deskripsi = "Berpengalaman Menggunakan Android Studio",
//            instansi = "Tech Company",
//            dibuat = "2024-10-21",
//            status = true
//        ),
//        Loker(
//            title = "Backend Engineer",
//            gaji = 10000000,
//            deskripsi = "Berpengalaman Menggunakan Express Js",
//            instansi = "Tech Company",
//            dibuat = "2024-10-21",
//            status = true
//        )
//    )
//    @Test
//    @Throws(Exception::class)
//    fun insertAndRetrieve() = runBlocking {
//        loker.forEach { lokerDao.insert(it) }
//        val allLoker = lokerDao.getAllLoker()
//        assert(allLoker.size == 3)
//        allLoker.forEach { loker ->
//            println("Loker: $loker")
//        }
//    }
//
//}