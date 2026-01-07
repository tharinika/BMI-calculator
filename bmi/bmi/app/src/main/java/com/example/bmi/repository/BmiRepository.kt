package com.example.bmi.repository

import android.content.Context
import com.example.bmi.data.BmiDao
import com.example.bmi.data.BmiDatabase
import com.example.bmi.data.BmiEntity
import kotlinx.coroutines.flow.Flow

class BmiRepository(context: Context) {
    private val bmiDao: BmiDao = BmiDatabase.getDatabase(context).bmiDao()

    val allBmiRecords: Flow<List<BmiEntity>> = bmiDao.getAllBmiRecords()

    suspend fun insertBmi(bmi: BmiEntity) {
        bmiDao.insertBmi(bmi)
    }
}
