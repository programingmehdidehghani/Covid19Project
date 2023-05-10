package com.example.covid19project.api

import retrofit2.http.GET

interface Covid19IndiaApiService {

/*    @GET("data.json")
    suspend fun getData():*/

    companion object {
        const val BASE_URL = "https://api.covid19india.org/"
    }
}