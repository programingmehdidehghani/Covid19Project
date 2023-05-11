package com.example.covid19project.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.covid19project.databinding.ActivityMainBinding
import com.example.covid19project.databinding.ActivityStateDetailsBinding

class MainActivity : AppCompatActivity() {

    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}