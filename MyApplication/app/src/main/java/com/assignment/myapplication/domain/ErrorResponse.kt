package com.assignment.myapplication.domain

import com.google.gson.annotations.SerializedName

/**
 * A data class used to handle Api error response
 */
data class ErrorResponse(@SerializedName("error_id") val errorCode: Int,
                         @SerializedName("error_message") val errorMessage: String,
                         @SerializedName("error_name") val errorName: String)
