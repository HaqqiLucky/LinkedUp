package com.example.linkedup.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun insert(user: User) {
        return withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    suspend fun getAllUser(): List<User> {
        return userDao.getAllUser()
    }
}