package com.example.covid19project.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19project.model.StateDetailsResponse
import com.example.covid19project.repository.CovidIndiaRepository
import com.example.covid19project.util.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StateDetailsViewModel(private val covidIndiaRepository: CovidIndiaRepository): ViewModel() {

    private val _stateCovidLiveData = MutableLiveData<State<StateDetailsResponse>>()

    val stateCovidLiveDataDetails: LiveData<State<StateDetailsResponse>> = _stateCovidLiveData

    fun getDistrictData(state: String){
         viewModelScope.launch {
             covidIndiaRepository.getStateDetailsData(state).collect {
                 _stateCovidLiveData.value = it
             }
         }
    }
}