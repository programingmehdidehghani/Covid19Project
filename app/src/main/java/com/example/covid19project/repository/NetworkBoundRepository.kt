package com.example.covid19project.repository

import androidx.annotation.MainThread
import com.example.covid19project.util.State
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.lang.Exception

abstract class NetworkBoundRepository<T> {

    fun asFlow() = flow<State<T>> {

        emit(State.loading())

        try {
            val apiResponse = fetchFromRemote()

            val remotePosts = apiResponse.body()

            if (apiResponse.isSuccessful && remotePosts != null) {
                emit(State.success(remotePosts))
            } else {
                emit(State.error(apiResponse.message()))
            }

        } catch (e: Exception) {
            emit(State.error("Network error! Can't get latest data."))
            e.printStackTrace()
        }
    }

    @MainThread
    abstract suspend fun fetchFromRemote(): Response<T>
}