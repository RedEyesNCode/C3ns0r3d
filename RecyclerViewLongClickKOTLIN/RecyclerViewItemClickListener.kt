package com.meyaoo.util

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView


open class RecyclerViewItemClickListener(val context: Context, val recyclerView: RecyclerView, var clickListenerRecyclerContactFragment: ClickListenerRecyclerContactFragment): RecyclerView.OnItemTouchListener,
    ClickListenerRecyclerContactFragment {


    lateinit var gestureDetector: GestureDetector
    var clickListenerRecyclerContactFragmentRecyclerView: ClickListenerRecyclerContactFragment = clickListenerRecyclerContactFragment

    init {
        gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                Log.i(Constant.DEV_ASHUTOSH, "onSingleTapUp:  ON SINGLE TAP GESTURE DETECTOR")

                return true
            }

            override fun onLongPress(e: MotionEvent) {
                //Find child on x and y position relative to screen
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null) {
                    Log.i(Constant.DEV_ASHUTOSH, "onLongPress:  ON LONG GESTURE DETECTOR")
                    clickListenerRecyclerContactFragmentRecyclerView.onLongClick(child, recyclerView.getChildLayoutPosition(child))
                }
            }
        })
    }


    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && gestureDetector.onTouchEvent(e)) {
            clickListenerRecyclerContactFragmentRecyclerView.onClick(child, rv.getChildLayoutPosition(child))
        }
        return false    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("Not yet implemented")
    }
}