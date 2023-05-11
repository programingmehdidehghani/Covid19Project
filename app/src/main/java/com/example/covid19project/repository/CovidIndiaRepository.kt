package com.example.covid19project.repository

import com.example.covid19project.api.Covid19IndiaApiService
import com.example.covid19project.model.StateResponse
import com.example.covid19project.util.State
import kotlinx.coroutines.flow.Flow

class CovidIndiaRepository(private val apiService: Covid19IndiaApiService) {

    fun getData(): Flow<State<StateResponse>>{
        return object
    }
}