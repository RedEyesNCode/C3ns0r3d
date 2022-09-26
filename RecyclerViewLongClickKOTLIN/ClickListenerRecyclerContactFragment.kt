package com.meyaoo.util

import android.view.View

interface ClickListenerRecyclerContactFragment {

        fun onClick(view: View?, position: Int){

        }
        fun onLongClick(view: View?, position: Int){
            // we are getting the adapter position here not the list position remember this

        }



}