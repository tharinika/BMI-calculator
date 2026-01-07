package com.example.bmi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.bmi.data.BmiEntity
import com.example.bmi.repository.BmiRepository

class BmiViewModel(private val repository: BmiRepository) : ViewModel() {

    // LiveData to observe BMI records
    val bmiRecords = repository.allBmiRecords

    // Function to calculate BMI and insert it into the database
    fun insertBmi(weight: Float, height: Float) {
        val bmi = weight / (height * height)
        val newRecord = BmiEntity(weight = weight, height = height, bmi = bmi)

        viewModelScope.launch {
            repository.insertBmi(newRecord)
        }
    }
}

// Factory class for BmiViewModel
class BmiViewModelFactory(private val repository: BmiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BmiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BmiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
