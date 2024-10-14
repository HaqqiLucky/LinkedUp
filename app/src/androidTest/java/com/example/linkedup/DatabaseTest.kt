//package com.example.linkedup
//
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class DatabaseTest {
//    @RunWith(AndroidJUnit4::class)
//    class DatabaseTest {
//
//        private lateinit val colorDao: ColorDao
//        private lateinit val db: ColorDatabase
//
//        private val red = Color(hex = "#FF0000", name = "red")
//        private val green = Color(hex = "#00FF00", name = "green")
//        private val blue = Color(hex = "#0000FF", name = "blue")
//
//        @Before
//        fun createDb() {
//            val context: Context = ApplicationProvider.getApplicationContext()
//            db = Room.inMemoryDatabaseBuilder(context, LokerDatabase::class.java)
//                .allowMainThreadQueries()
//                .build()
//            colorDao = db.colorDao()
//        }
//        @After
//        @Throws(IOException::class)
//        fun closeDb() = db.close()
//        @Test
//        @Throws(Exception::class)
//        fun insertAndRetrieve() {
//            colorDao.insert(red, green, blue)
//            val colors = colorDao.getAll()
//            assert(colors.size == 3)
//        }
//    }
//}