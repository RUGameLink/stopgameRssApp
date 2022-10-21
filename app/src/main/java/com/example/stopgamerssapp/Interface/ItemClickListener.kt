package com.example.stopgamerssapp.Interface

import android.view.View

interface ItemClickListener {
    fun itemOnClick(position: Int){}

    fun itemOnLongClick(position: Int){}
}