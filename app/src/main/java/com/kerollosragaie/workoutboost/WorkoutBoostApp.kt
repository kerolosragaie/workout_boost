package com.kerollosragaie.workoutboost

import android.app.Application
import com.kerollosragaie.workoutboost.database.HistoryDatabase

class WorkoutBoostApp : Application() {
    val historyDb by lazy {
        HistoryDatabase.getInstance(this)
    }
}