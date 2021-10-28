package com.app.myworkoutapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton INSTANCE object.
 */

@Database(entities = [ActivityMC::class], version = 1, exportSchema = false)
abstract class ActivityRoomDB : RoomDatabase() {

    abstract fun itemDao(): ActivityDAO

    companion object {
        @Volatile
        private var INSTANCE: ActivityRoomDB? = null

        fun getDatabase(context: Context): ActivityRoomDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ActivityRoomDB::class.java,
                    "activity_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}