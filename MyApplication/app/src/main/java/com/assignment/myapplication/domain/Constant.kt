package com.assignment.myapplication.domain

import android.view.View
import com.google.gson.Gson

object Constant {
    fun getErrorResponse(response:String):ErrorResponse{
        return Gson().fromJson(response, ErrorResponse::class.java)
    }
    fun View.setVisible(visible: Boolean) {
        visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}