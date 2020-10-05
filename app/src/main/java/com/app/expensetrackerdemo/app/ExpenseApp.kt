package com.app.expensetrackerdemo.app

import android.app.Application
import com.app.expensetrackerdemo.utility.PreferenceManager

class ExpenseApp: Application() {
    override fun onCreate() {
        super.onCreate()
        PreferenceManager.initPreferences(context = applicationContext)
    }
}