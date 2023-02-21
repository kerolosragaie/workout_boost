package com.kerollosragaie.workoutboost.database

import androidx.room.*
import com.kerollosragaie.workoutboost.models.Exercise
import kotlinx.coroutines.flow.Flow

//DAO (Data Access Object) : inside it we define the funs to be used in Database table (History table db)
@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Update
    suspend fun update(historyEntity: HistoryEntity)

    @Delete
    suspend fun delete(historyEntity: HistoryEntity)

    //Flow: when value changes it changes immediately (live update)
    @Query(value = "SELECT * FROM history_table")
    fun fetchAllHistory(): Flow<List<HistoryEntity>>

}