package com.example.covid19project.api

import com.example.covid19project.model.StateDetailsResponse
import com.example.covid19project.model.StateResponse
import retrofit2.Response
import retrofit2.http.GET

interface Covid19IndiaApiService {

    @GET("data.json")
    suspend fun getData(): Response<StateResponse>

    @GET("v2/state_district_wise.json")
    suspend fun getStateDistrictData(): Response<List<StateDetailsResponse>>

    companion object {
        const val BASE_URL = "https://api.covid19india.org/"
    }
}