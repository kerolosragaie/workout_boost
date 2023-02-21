package com.kerollosragaie.workoutboost.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun historyDao() : HistoryDao

    companion object{
        @Volatile
        private var INSTANCE: HistoryDatabase?=null

        fun getInstance(context: Context):HistoryDatabase{
            /*? Multiple threads may ask for database at the same time and to ensure
            ? we only initialize it once by using synchronized() */
            synchronized(this){
                var instance = INSTANCE
                if(instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java,
                        "history_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}