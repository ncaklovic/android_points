package com.crobridge.points

import android.app.Application
import androidx.lifecycle.*
import com.crobridge.points.db.Point
import com.crobridge.points.db.Polyline
//import androidx.lifecycle.Transformations
import com.crobridge.points.db.PointDao
import kotlinx.coroutines.launch

class PointViewModel(val db: PointDao, app: Application) : AndroidViewModel(app) {

    private val current_polyline = MutableLiveData<Long>(0)
    private suspend fun insert(p : Point) {
        db.insert(p)
    }

    fun addPoint(p : Point){
        current_polyline?.let {
            if(it.value!! != 0L){
                p.polyline_id = it.value!!
                viewModelScope.launch {
                    insert(p)
                }
            }
        }
    }

    // switchMap -> refresh for new id
    val points = current_polyline.switchMap {  current_polyline ->
        db.getAllPoints(current_polyline)
    }

    private suspend fun insert(p : Polyline) {
        current_polyline.value = db.insert(p)
    }

    fun addPolyline(p : Polyline){
        viewModelScope.launch {
            insert(p)
        }
    }

    val polylines = db.getAllPolylines()

    fun setPolylineId(polyline_id : Long){
        current_polyline.value = polyline_id
    }

}