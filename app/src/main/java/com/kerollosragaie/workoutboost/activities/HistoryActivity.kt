package com.kerollosragaie.workoutboost.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kerollosragaie.workoutboost.WorkoutBoostApp
import com.kerollosragaie.workoutboost.adapters.HistoryAdapter
import com.kerollosragaie.workoutboost.database.HistoryDao
import com.kerollosragaie.workoutboost.database.HistoryEntity
import com.kerollosragaie.workoutboost.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHistoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingUpToolBar()

        val historyDao = (application as WorkoutBoostApp).historyDb.historyDao()
        getHistory(historyDao)

    }

    //*** For app toolbar
    private fun settingUpToolBar() {
        setSupportActionBar(binding.toolbarHistory)
        //*** shows top back button
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbarHistory.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    //*** For database
    private fun getHistory(historyDao: HistoryDao){
        lifecycleScope.launch {
            historyDao.fetchAllHistory().collect{historyList ->
                if(historyList.isNotEmpty()){
                    binding.tvNoDataAvailable.visibility = View.GONE
                    binding.rvHistory.visibility = View.VISIBLE

                    val datesList = ArrayList(historyList)
                    Log.w("TEST",datesList.toString())
                    binding.rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    val historyAdapter = HistoryAdapter(datesList)
                    binding.rvHistory.adapter = historyAdapter
                }else{
                    binding.rvHistory.visibility = View.GONE
                    binding.tvNoDataAvailable.visibility = View.VISIBLE
                }
            }
        }
    }
}