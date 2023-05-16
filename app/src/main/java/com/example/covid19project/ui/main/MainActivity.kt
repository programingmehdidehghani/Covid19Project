package com.example.covid19project.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import com.example.covid19project.R
import com.example.covid19project.databinding.ActivityMainBinding
import com.example.covid19project.databinding.ActivityStateDetailsBinding
import com.example.covid19project.ui.adapter.TotalAdapter
import com.example.covid19project.ui.main.adapter.ItemAdapter
import com.example.covid19project.util.State
import com.example.covid19project.util.getPeriod
import com.example.covid19project.util.toDateFormat
import com.google.android.material.snackbar.Snackbar
import java.sql.Date

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val mTotalAdapter = TotalAdapter()
    private val mStateAdapter = ItemAdapter(this::navigateToStateDetailsActivity)
    private val adapter = MergeAdapter(mTotalAdapter, mStateAdapter)

    private var backPressedTime = 0L
    private val backSnackbar by lazy {
        Snackbar.make(viewBinding.root, BACK_PRESSED_MESSAGE, Snackbar.LENGTH_SHORT)
    }

    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }

    private fun initViews() {
        setSupportActionBar(viewBinding.appBarlayout.toolbar)
        viewBinding.recycler.adapter = adapter

        viewBinding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
    }

    private fun initData() {
        viewModel.covidLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> viewBinding.swipeRefreshLayout.isRefreshing = true
                is State.Error -> {
                    viewBinding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(applicationContext, state.message, Toast.LENGTH_LONG).show()
                }
                is State.Success -> {
                    viewBinding.swipeRefreshLayout.isRefreshing = false
                    val list = state.data.stateWiseDetails
                    mTotalAdapter.submitList(list.subList(0,1))
                    mStateAdapter.submitList(list.subList(1, list.size -1))

                    supportActionBar?.subtitle = getString(
                        R.string.text_last_updated,
                        getPeriod(
                            list[0].lastUpdatedTime.toDateFormat() as Date
                        )
                    )
                }
            }
        })
        if (viewModel.covidLiveData.value !is State.Success){
            loadData()
        }
    }

    private fun loadData() {
        viewModel.getData()
    }

    companion object {
        const val JOB_TAG = "notificationWorkTag"
        const val BACK_PRESSED_MESSAGE = "Press back again to exit"
    }
}