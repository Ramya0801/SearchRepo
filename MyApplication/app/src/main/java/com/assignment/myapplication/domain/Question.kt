package com.assignment.myapplication.domain

import com.google.gson.annotations.SerializedName

data class Question(@SerializedName("title") val title: String,
                    @SerializedName("owner")  val owner: Owner,
                    @SerializedName("creation_date") val creationDate: Long,
                    @SerializedName("link") val link: String)

data class Owner(@SerializedName("display_name") val displayName: String)

data class SearchResponse(val items:List<Question>)

