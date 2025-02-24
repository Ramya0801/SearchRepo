package com.assignment.myapplication.data.network

import com.assignment.myapplication.domain.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun search( @Query("order") order: String,
                        @Query("sort") sort: String,
                        @Query("intitle") keyword: String,
                        @Query("site") site: String ) : Response<SearchResponse>
}