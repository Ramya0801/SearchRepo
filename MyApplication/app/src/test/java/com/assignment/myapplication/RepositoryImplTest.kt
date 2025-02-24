package com.assignment.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.assignment.myapplication.data.NetworkResultState
import com.assignment.myapplication.data.network.ApiService
import com.assignment.myapplication.data.repository.RepositoryImpl
import com.assignment.myapplication.domain.Owner
import com.assignment.myapplication.domain.Question
import com.assignment.myapplication.domain.SearchResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import retrofit2.Response


@ExperimentalCoroutinesApi
class RepositoryImplTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repositoryImpl: RepositoryImpl


    private val apiService: ApiService = mock()

    @Before
    fun setup() {
        repositoryImpl = RepositoryImpl(apiService)
    }

    @Test
    fun testResultForSuccessResponse() = runTest {
        // Mock the API response
        val mockOwner = Owner("Gad")
        val mockQuestions =
            Question(
                "Should I use the datetime or timestamp data type in MySQL?",
                mockOwner,
                1234567890,
                "https://stackoverflow.com/users/25152/gad"
            )

        val mockResponse = SearchResponse(listOf(mockQuestions))
        val mockApiResponse = Response.success(mockResponse)

        // Mock the API service call to return the successful response
        Mockito.`when`(apiService.search(
            order = "desc",
            sort = "relevance",
            keyword = "data",
            site = "stackoverflow"
        )).thenReturn(mockApiResponse)

        // Call the method
        val result = repositoryImpl.getSearchResults("data")

        // Verify the result is a success
        assertTrue(result is NetworkResultState.Success)
    }

    @Test
    fun testResultForErrorResponse() = runTest {
        // Mock the error response body
        val errorResponseBody = ResponseBody.create(null, "{\"error_id\": 404, \"error_message\": \"order\", \"error_name\": \"bad_parameter\"}")
        val mockApiResponse = Response.error<SearchResponse>(404, errorResponseBody)

        // Mock the API service call to return the error response
        Mockito.`when`(apiService.search(
            order = "descc",
            sort = "relevance",
            keyword = "da",
            site = "stackoverflow"
        )).thenReturn(mockApiResponse)

        // Call the method
        val result = repositoryImpl.getSearchResults("da")

        // Verify the result is an error
        assertTrue(result is NetworkResultState.Error)
    }

    @Test
    fun testResultWithException() = runTest {
        // Simulate an exception (e.g., network failure)
        Mockito.`when`(apiService.search(
            order = "",
            sort = "relevance",
            keyword = "key",
            site = "stackoverflow"
        )).thenThrow(RuntimeException("Network error"))

        // Call the method
        val result = repositoryImpl.getSearchResults("key")

        // Verify the result is an error
        assertTrue(result is NetworkResultState.Error)
        assertEquals("Failed to fetch data", (result as NetworkResultState.Error).error)
    }

}