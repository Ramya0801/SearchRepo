package com.assignment.myapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.myapplication.data.NetworkResultState
import com.assignment.myapplication.domain.Question
import com.assignment.myapplication.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: Repository) : ViewModel(){

    private var searchResults = MutableLiveData<NetworkResultState<List<Question>>>()
    val questions: LiveData<NetworkResultState<List<Question>>> get() = searchResults

    fun searchResults(key: String){
       searchResults.postValue(NetworkResultState.Loading)
       viewModelScope.launch(Dispatchers.IO) {
           try {
               searchRepository.getSearchResults(key).let { response->
                   if(response is NetworkResultState.Success ){
                       searchResults.postValue(NetworkResultState.Success( response.data.items ))
                   } else if(response is NetworkResultState.Error){
                       searchResults.postValue(NetworkResultState.Error(response.error))
                   }
               }
           } catch (e: IOException) {
               // Network error
               searchResults.postValue(NetworkResultState.Error("Network error: Unable to connect to the server"))
           } catch (e: HttpException) {
               // Server responded with an error
               searchResults.postValue(NetworkResultState.Error("Server error: ${e.message}"))
           } catch (e: Exception){
               searchResults.postValue(NetworkResultState.Error("Failed to fetch data"))
           }

       }
    }

}