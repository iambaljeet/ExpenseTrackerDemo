package com.app.expensetrackerdemo.utility

import android.content.Context
import android.content.SharedPreferences
import com.app.expensetrackerdemo.helper.IS_CONTACT_PERMISSIONS_REQUESTED
import com.app.expensetrackerdemo.helper.SHARED_PREFERENCES_NAME

object PreferenceManager {
    var sharedPreferences: SharedPreferences? = null

    @Synchronized
    fun initPreferences(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
    }

    fun setIsContactPermissionRequested(isPermissionsRequested: Boolean) {
        sharedPreferences?.edit()
            ?.putBoolean(IS_CONTACT_PERMISSIONS_REQUESTED, isPermissionsRequested)
            ?.apply()
    }

    fun getIsContactPermissionRequested(): Boolean {
        return sharedPreferences
            ?.getBoolean(IS_CONTACT_PERMISSIONS_REQUESTED, false) ?: false
    }
}