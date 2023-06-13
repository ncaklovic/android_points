
package com.crobridge.points.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "point")
data class Point(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo(name = "x")
        var x: Float = 0F,

        @ColumnInfo(name = "y")
        var y: Float = 0F
)
