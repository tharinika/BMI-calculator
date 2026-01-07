package com.example.bmi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bmi.data.AppDatabase
import com.example.bmi.data.User
import com.example.bmi.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(getApplication())  // ✅ FIXED! Correct context
    private val userDao = database.userDao()
    private val repository = UserRepository(userDao)

    fun registerUser(username: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val existingUser = repository.getUser(username, password)
            if (existingUser == null) {
                repository.insertUser(User(username = username, password = password))
                onSuccess()
            } else {
                onFailure("User already exists!")
            }
        }
    }

    fun loginUser(username: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUser(username, password)
            if (user != null) {
                onSuccess()
            } else {
                onFailure("Invalid Credentials!")
            }
        }
    }
}
