package com.rexdev.tasty_trends.global

import android.app.Application

class Variables : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize the global variables
        GlobalVariables.initialize(this) // Pass the application context

        // Initialize the lists
        GlobalVariables.CARTLIST = mutableListOf()
        GlobalVariables.FAVLIST = mutableListOf()

        // Initialize other properties if needed
        GlobalVariables.PROFILE_ID = "roy_YouCurrentlyHaveNoProfileID12345"
        GlobalVariables.PROFILE_IMG = null
        GlobalVariables.PROFILE_NAME = null
        GlobalVariables.SHOP_ID = null
        GlobalVariables.HOST_URL = "http://192.168.87.197:80/"
    }
}
