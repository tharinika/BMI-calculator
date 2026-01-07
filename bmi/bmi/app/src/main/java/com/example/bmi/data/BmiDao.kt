package com.example.bmi.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BmiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBmi(bmi: BmiEntity)

    @Query("SELECT * FROM bmi_records ORDER BY id DESC")
    fun getAllBmiRecords(): Flow<List<BmiEntity>>
}
