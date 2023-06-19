
package com.crobridge.points.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "polyline")
data class Polyline(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo(name = "start_time")
        val startTimeMilliSec: Long = System.currentTimeMillis(),

        @ColumnInfo(name = "name")
        var name: String = ""
)
