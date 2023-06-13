
package com.crobridge.points

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crobridge.points.db.PointDao
import com.crobridge.points.PointViewModel

class ViewModelFactory(
        private val dataSource: PointDao,
        private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PointViewModel::class.java)) {
            return PointViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

