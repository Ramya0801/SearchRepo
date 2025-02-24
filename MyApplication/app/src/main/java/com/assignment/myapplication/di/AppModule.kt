package com.assignment.myapplication.di

import com.assignment.myapplication.data.network.ApiService
import com.assignment.myapplication.data.repository.RepositoryImpl
import com.assignment.myapplication.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    private  val baseURL = "https://api.stackexchange.com/2.3/"
    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService = retrofit.create(
        ApiService::class.java)


    @Provides
    @Singleton
    fun providerRepositoryImpl(apiService: ApiService) : Repository {
        return RepositoryImpl(apiService)
    }

}