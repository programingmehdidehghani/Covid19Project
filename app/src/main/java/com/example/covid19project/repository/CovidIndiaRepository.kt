package com.example.covid19project.repository

import com.example.covid19project.api.Covid19IndiaApiService
import com.example.covid19project.model.StateDetailsResponse
import com.example.covid19project.model.StateResponse
import com.example.covid19project.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response

class CovidIndiaRepository(private val apiService: Covid19IndiaApiService) {

    fun getData(): Flow<State<StateResponse>>{
        return object: NetworkBoundRepository<StateResponse>() {
            override suspend fun fetchFromRemote(): Response<StateResponse> = apiService.getData()
        }.asFlow().flowOn(Dispatchers.IO)
    }

    fun getStateDetailsData(stateName: String): Flow<State<StateDetailsResponse>> {
        return object : NetworkBoundRepository<List<StateDetailsResponse>>() {
            override suspend fun fetchFromRemote(): Response<List<StateDetailsResponse>> = apiService.getStateDistrictData()
        }.asFlow().flowOn(Dispatchers.IO).map { state ->
            when(state){
                is State.Loading -> State.loading()
                is State.Success -> {
                    val data = state.data.find { it.state == stateName }

                    if (data != null){
                        State.success<StateDetailsResponse>(data)
                    } else {
                        State.error<StateDetailsResponse>("No data found of state '$stateName'")
                    }
                }
                is State.Error -> {
                    State.error<StateDetailsResponse>(state.message)
                }
            }
        }
    }
}