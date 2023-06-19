package com.crobridge.points

import android.app.Application
import androidx.lifecycle.*
import com.crobridge.points.db.Point
import com.crobridge.points.db.Polyline
//import androidx.lifecycle.Transformations
import com.crobridge.points.db.PointDao
import kotlinx.coroutines.launch

class PointViewModel(val db: PointDao, app: Application) : AndroidViewModel(app) {

    private suspend fun insert(p : Point) {
        db.insert(p)
    }

    fun addPoint(p : Point){
        viewModelScope.launch {
            insert(p)
        }
    }

    val points = db.getAllPoints()


    private suspend fun insert(p : Polyline) {
        db.insert(p)
    }

    fun addPolyline(p : Polyline){
        viewModelScope.launch {
            insert(p)
        }
    }

    val polylines = db.getAllPolylines()

}