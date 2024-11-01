package com.rexdev.tasty_trends.global

import android.app.Application
import com.rexdev.tasty_trends.domain.RetrofitInstance

class Variables : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize the global variables with the application context
        GlobalVariables.initialize(this)

        // Initialize the lists and other properties
        initializeGlobalVariables()
    }

    private fun initializeGlobalVariables() {
        // Ensure lists are initialized before clearing
        GlobalVariables.CARTLIST = mutableListOf()
        GlobalVariables.FAVLIST = mutableListOf()

        // Set default values
        GlobalVariables.PROFILE_ID = "roy_YouCurrentlyHaveNoProfileID12345"
        GlobalVariables.PROFILE_IMG = null
        GlobalVariables.PROFILE_NAME = null
        GlobalVariables.PROFILE_EMAIL = null
        GlobalVariables.PROFILE_TYPE = null
        GlobalVariables.SHOP_ID = null
        GlobalVariables.HOST_URL = "http://192.168.149.197:80/"
        RetrofitInstance.initialize(this)
    }
}
