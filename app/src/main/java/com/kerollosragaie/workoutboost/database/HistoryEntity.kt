package com.kerollosragaie.workoutboost.database


import androidx.room.Entity
import androidx.room.PrimaryKey

//Entity: creates new db table
@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey
    val date:String,
)
