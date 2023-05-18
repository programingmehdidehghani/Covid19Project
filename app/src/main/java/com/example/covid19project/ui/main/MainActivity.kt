package com.example.covid19project.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.MergeAdapter
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.covid19project.R
import com.example.covid19project.databinding.ActivityMainBinding
import com.example.covid19project.model.Details
import com.example.covid19project.ui.adapter.TotalAdapter
import com.example.covid19project.ui.detail.StateDetailsActivity
import com.example.covid19project.ui.main.adapter.ItemAdapter
import com.example.covid19project.util.State
import com.example.covid19project.util.applyTheme
import com.example.covid19project.util.getPeriod
import com.example.covid19project.util.isDarkTheme
import com.example.covid19project.util.toDateFormat
import com.example.covid19project.worker.NotificationWorker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import java.sql.Date
import java.util.concurrent.TimeUnit


@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {

    //private val viewModel: MainViewModel by viewModels()
    private lateinit var viewModel: MainViewModel


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
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initViews()
        initData()
        initWorker()
    }

    private fun initViews() {
        setSupportActionBar(viewBinding.appBarlayout.toolbar)
        viewBinding.recycler.adapter = adapter

        viewBinding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.toolbar_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_uimode -> {
                val uiMode = if (isDarkTheme()){
                    AppCompatDelegate.MODE_NIGHT_NO
                } else {
                    AppCompatDelegate.MODE_NIGHT_YES
                }
                applyTheme(uiMode)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToStateDetailsActivity(details: Details) {
        startActivity(Intent(this, StateDetailsActivity::class.java).apply {
            putExtra(StateDetailsActivity.KEY_STATE_DETAILS, details)
        })
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

    private fun initWorker(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            JOB_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        )
    }

    private fun loadData() {
        viewModel.getData()
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backSnackbar.dismiss()
            super.onBackPressed()
            return
        } else {
            backSnackbar.show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    companion object {
        const val JOB_TAG = "notificationWorkTag"
        const val BACK_PRESSED_MESSAGE = "Press back again to exit"
    }
}