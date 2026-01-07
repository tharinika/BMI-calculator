package com.example.bmi.repository

import com.example.bmi.data.User
import com.example.bmi.data.UserDao

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun getUser(username: String, password: String) = userDao.getUser(username, password)
}
