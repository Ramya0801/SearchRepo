package com.assignment.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.assignment.myapplication.data.NetworkResultState
import com.assignment.myapplication.domain.Owner
import com.assignment.myapplication.domain.Question
import com.assignment.myapplication.domain.Repository
import com.assignment.myapplication.domain.SearchResponse
import com.assignment.myapplication.presentation.viewmodel.SearchViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import java.io.IOException


@ExperimentalCoroutinesApi
class SearchViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

     private  val repository: Repository = mock()

    @Mock
    lateinit var searchViewModel: SearchViewModel

    @Before
    fun setup() {
        searchViewModel = SearchViewModel(repository)

    }


    @Test
    fun testsearchKeyNull() = runBlockingTest {
        // Arrange
        val errorResponse = NetworkResultState.Error("Error message")

        // Mock the repository call
        `when`(repository.getSearchResults("")).thenReturn(errorResponse)

        // Act
        val observer: Observer<NetworkResultState<List<Question>>> = mock()
        searchViewModel.questions.observeForever(observer)
        searchViewModel.searchResults("")

        // Assert
        verify(observer).onChanged(NetworkResultState.Loading)
        verify(observer).onChanged(NetworkResultState.Error("Error message"))
    }

    @Test
    fun testsearchResultssuccess() = runBlockingTest {

        val mockOwner = Owner("John")
        val questionList = Question( title = "Kotlin", owner =mockOwner, creationDate = 1633496320, link = "https://stackoverflow.com/questions/12345")
        val mockQuestions = listOf(
            Question(
                "Should I use the datetime or timestamp data type in MySQL?",
                mockOwner,
                1234567890,
                "https://stackoverflow.com/users/25152/gad"
            )
        )
        val searchResponse = SearchResponse(mockQuestions)
        val successResponse = NetworkResultState.Success(searchResponse)
        // Mock the repository call
        `when`(repository.getSearchResults("Kotlin")).thenReturn(successResponse)

        // Act
        val observer: Observer<NetworkResultState<List<Question>>> = mock()
        searchViewModel.questions.observeForever(observer)
        searchViewModel.searchResults("Kotlin")

        // Assert
        verify(observer).onChanged(NetworkResultState.Loading)
        verify(observer).onChanged(NetworkResultState.Success(mockQuestions))
    }

    @Test
    fun testsearchResultserror() = runBlockingTest {
        // Arrange
        val errorResponse = NetworkResultState.Error("Error message")

        // Mock the repository call
        `when`(repository.getSearchResults("Kotlin")).thenReturn(errorResponse)

        // Act
        val observer: Observer<NetworkResultState<List<Question>>> = mock()
        searchViewModel.questions.observeForever(observer)
        searchViewModel.searchResults("Kotlin")

        // Assert
        verify(observer).onChanged(NetworkResultState.Loading)
        verify(observer).onChanged(NetworkResultState.Error("Error message"))
    }


    @Test
    fun testsearchResultsserverexception() = runBlockingTest {
        // Arrange
        `when`(repository.getSearchResults("Kotlin")).thenThrow(HttpException(mock()))

        // Act
        val observer: Observer<NetworkResultState<List<Question>>> = mock()
        searchViewModel.questions.observeForever(observer)
        searchViewModel.searchResults("Kotlin")

        // Assert
        verify(observer).onChanged(NetworkResultState.Loading)
        verify(observer).onChanged(NetworkResultState.Error("Server error: null"))
    }


}
