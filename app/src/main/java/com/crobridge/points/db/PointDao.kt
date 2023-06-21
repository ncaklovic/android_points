
package com.crobridge.points.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query

@Dao
interface PointDao {

    @Insert
    suspend fun insert(p: Point) : Long

    @Query("SELECT * FROM point WHERE polyline = :polyline_id")
    fun getAllPoints(polyline_id: Long): LiveData<List<Point>>

    @Insert
    suspend fun insert(p: Polyline) : Long

    @Query("SELECT * FROM polyline")
    fun getAllPolylines(): LiveData<List<Polyline>>

    @Query("SELECT * FROM polyline WHERE id = :polyline_id")
    fun getCurrent(polyline_id: Long): LiveData<Polyline>

    @Query("SELECT AVG(x) AS avg_x, AVG(y) AS avg_y FROM point WHERE polyline = :polyline_id")
    fun getTotal(polyline_id: Long): LiveData<Total>

    @Query("DELETE FROM point WHERE id = (SELECT MAX(id) FROM point WHERE polyline = :polyline_id)")
    fun deleteLastPoint(polyline_id: Long)

}
