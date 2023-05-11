package com.example.covid19project.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.covid19project.databinding.ActivityStateDetailsBinding

class StateDetailsActivity: AppCompatActivity() {

    private val viewBinding by lazy {
        ActivityStateDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}