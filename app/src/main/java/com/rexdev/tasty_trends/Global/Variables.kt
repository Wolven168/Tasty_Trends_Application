package com.rexdev.tasty_trends.Global

import android.app.Application

class Variables : Application(){
    override fun onCreate() {
        super.onCreate()
        // Initialize the lists
        GlobalVariables.CARTLIST = mutableListOf()
        GlobalVariables.FAVLIST = mutableListOf()
        GlobalVariables.PROFILE_ID
        GlobalVariables.PROFILE_IMG
        GlobalVariables.PROFILE_NAME
    }

}