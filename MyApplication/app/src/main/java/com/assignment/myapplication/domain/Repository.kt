package com.assignment.myapplication.domain
import com.assignment.myapplication.data.NetworkResultState


interface Repository {
    suspend fun getSearchResults(keyword: String) : NetworkResultState<SearchResponse>
}