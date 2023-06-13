package com.crobridge.points

import android.app.Application
import androidx.lifecycle.*
import com.crobridge.points.db.Point
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
}