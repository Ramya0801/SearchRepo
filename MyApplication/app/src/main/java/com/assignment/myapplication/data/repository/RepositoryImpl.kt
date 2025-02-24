package com.assignment.myapplication.data.repository

import android.util.Log
import com.assignment.myapplication.data.NetworkResultState
import com.assignment.myapplication.data.network.ApiService
import com.assignment.myapplication.domain.Constant
import com.assignment.myapplication.domain.Repository
import com.assignment.myapplication.domain.SearchResponse
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: ApiService) : Repository {

    override suspend fun getSearchResults(keyword: String): NetworkResultState<SearchResponse> {
        val response =  apiService.search(
            order = "desc",
            sort = "relevance",
            keyword=keyword ,
            site = "stackoverflow"
        )

        return try {
            val result = response.body()
            //Handling api success responses (201, 200)
            if (response.isSuccessful && result!=null ) {
                NetworkResultState.Success(result)
            } else {
                //Handling api error response (501, 404)
                val errorBody = response.errorBody()
                // Convert errorBody to a String
                val errorString = errorBody?.string()
                val errorResponse = errorString?.let { it1 -> Constant.getErrorResponse(it1)}
                // Now you can access the fields in errorResponse
                Log.e("API_ERROR", "Error ID: ${errorResponse?.errorCode}")
                Log.e("API_ERROR", "Error Message: ${errorResponse?.errorName}")
                Log.e("API_ERROR", "Error Name: ${errorResponse?.errorMessage}")
                NetworkResultState.Error("${errorResponse?.errorName}")
            }
        } catch (e: Exception){
           NetworkResultState.Error("Failed to fetch data")
        }

    }
}