
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

    //   @Update
//    suspend fun update(p: Point)

//    @Query("DELETE FROM point")
//    suspend fun clear() : Int

    @Query("SELECT * FROM point")
    fun getAllPoints(): LiveData<List<Point>>

//    @Query("SELECT * FROM crobridge_points ORDER BY id DESC LIMIT 1")
//    suspend fun getLast(): Point?

//    @Query("SELECT * FROM crobridge_points WHERE id = :key")
//    fun getPoint(key: Long): LiveData<Point>
}
