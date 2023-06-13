package com.crobridge.points.ui.main

import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.Fragment
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.crobridge.points.PointViewModel
import com.crobridge.points.db.Point

class DrawView : View
{
    private val parent : DrawFragment

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        parent = getParentFragment(context) as DrawFragment
    }
    private fun getParentFragment(context: Context): Fragment? {
        var currentContext = context
        while (currentContext is ContextWrapper) {
            if (currentContext is FragmentActivity) {
                val fragmentManager = currentContext.supportFragmentManager
                val fragments = fragmentManager.fragments
                return fragments[0] // ??
                for (fragment in fragments) {
                    if (fragment.view?.findViewWithTag<View>(tag) != null) {
                        return fragment
                    }
                }
            }
            currentContext = currentContext.baseContext
        }
        return null
    }

    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        parent.viewModel.points.value?.forEach{
            canvas.drawCircle(it.x, it.y, 10f, textPaint)
        }
    }

    private val myListener =  object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            Log.i("DrawView", "onDown")
            return true
        }
    }

    private val detector: GestureDetector = GestureDetector(context, myListener)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.i("DrawView", "onTouchEvent:" + event.x + " " + event.y)
        val p = Point()
        p.x = event.x
        p.y = event.y
        parent.viewModel.addPoint(p)
        return super.onTouchEvent(event)
    }

}
