package com.example.covid19project.di

import com.example.covid19project.ui.detail.StateDetailsViewModel
import com.example.covid19project.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { MainViewModel(get())}
    viewModel { StateDetailsViewModel(get()) }
}