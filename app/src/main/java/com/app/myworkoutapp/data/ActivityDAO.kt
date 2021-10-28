package com.app.myworkoutapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDAO {

    @Query("SELECT * from ActivityMC ORDER BY id ASC")
    fun getItems(): Flow<List<ActivityMC>>

    @Query("SELECT * from ActivityMC WHERE id = :id")
    fun getItem(id: Int): Flow<ActivityMC>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(activityMC: ActivityMC)

    @Update
    suspend fun update(activityMC: ActivityMC)

    @Delete
    suspend fun delete(activityMC: ActivityMC)
}