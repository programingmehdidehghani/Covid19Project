package com.example.covid19project.ui.detail

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.MergeAdapter
import com.example.covid19project.R
import com.example.covid19project.databinding.ActivityStateDetailsBinding
import com.example.covid19project.model.Details
import com.example.covid19project.ui.adapter.TotalAdapter
import com.example.covid19project.ui.detail.adapter.DistrictsAdapter
import com.example.covid19project.util.State
import com.example.covid19project.util.getPeriod
import com.example.covid19project.util.toDateFormat
import java.sql.Date

@Suppress("DEPRECATION")
class StateDetailsActivity: AppCompatActivity() {

    private val viewBinding by lazy {
        ActivityStateDetailsBinding.inflate(layoutInflater)
    }

    private val mStateTotalAdapter = TotalAdapter()
    private val mDistrictAdapter = DistrictsAdapter()

    private val mergeAdapter = MergeAdapter(mStateTotalAdapter,mDistrictAdapter)

    private val viewModel: StateDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }

    private fun getStateDetails(): Details? = intent.getParcelableExtra(KEY_STATE_DETAILS)

    private fun initViews(){
        setSupportActionBar(viewBinding.appBarlayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewBinding.recyclerState.adapter = mergeAdapter

        val details: Details? = getStateDetails()

        details?.let {
            mStateTotalAdapter.submitList(listOf(it))

            supportActionBar?.title = it.state
            supportActionBar?.subtitle = getString(
                R.string.text_last_updated,
                getPeriod(
                    it.lastUpdatedTime.toDateFormat() as Date
                )
            )
        }

        viewBinding.swipeRefreshLayout.setOnRefreshListener {

        }
    }

    private fun initData(){
        viewModel.stateCovidLiveDataDetails.observe(this, Observer { state ->
            when(state){
               is State.Loading -> {
                   viewBinding.swipeRefreshLayout.isRefreshing = true
               }
                is State.Success -> {
                    val list = state.data.districtData
                    list.sortedByDescending { it.confirmed }.let { districtList ->
                        mDistrictAdapter.submitList(districtList)
                    }

                    viewBinding.swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    companion object {
        const val KEY_STATE_DETAILS = "key_state_details"
    }
}