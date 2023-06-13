package com.crobridge.points.ui.main

import android.app.Application
import androidx.lifecycle.*
//import androidx.lifecycle.Transformations

class PageViewModel() : ViewModel() {

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = _index.map {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}