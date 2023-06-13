package com.crobridge.points.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Point::class], version = 1, exportSchema = false)
abstract class PointDb : RoomDatabase() {

    abstract val pointDbDao: PointDao

    companion object {
        @Volatile
        private var INSTANCE: PointDb? = null

        fun getInstance(context: Context): PointDb {
            synchronized(this) {
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            PointDb::class.java,
                            "crobridge_point_db_2"
                    )
                            .fallbackToDestructiveMigration()
                            //.allowMainThreadQueries()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
