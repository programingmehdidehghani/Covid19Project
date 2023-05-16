package com.example.covid19project.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19project.model.StateResponse
import com.example.covid19project.repository.CovidIndiaRepository
import com.example.covid19project.util.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel (private val repository: CovidIndiaRepository): ViewModel() {

    private val _covidLiveData = MutableLiveData<State<StateResponse>>()
    val covidLiveData : LiveData<State<StateResponse>> = _covidLiveData

    fun getData(){
        viewModelScope.launch {
            repository.getData().collect{
                _covidLiveData.value = it
            }
        }
    }
}