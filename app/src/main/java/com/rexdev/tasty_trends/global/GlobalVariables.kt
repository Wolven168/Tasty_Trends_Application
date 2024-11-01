package com.rexdev.tasty_trends.global

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.rexdev.tasty_trends.activity.IntroActivity
import com.rexdev.tasty_trends.dataClass.CartItem
import com.rexdev.tasty_trends.dataClass.ShopItem
import com.rexdev.tasty_trends.dataClass.Ticket
import java.io.File

object GlobalVariables {
    private lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context.applicationContext
        CARTLIST = mutableListOf() // Initialize CARTLIST
        FAVLIST = mutableListOf() // Initialize FAVLIST
    }

    fun logout(context: Context) {
        PROFILE_ID = null
        PROFILE_IMG = null
        PROFILE_NAME = null
        PROFILE_EMAIL = null
        val intent = Intent(context, IntroActivity::class.java)

        // Use context to start the activity
        if (context is Activity) {
            context.startActivity(intent)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Needed if context is not an Activity
            context.startActivity(intent)
        }
    }

    val cacheDir: File
        get() = appContext.cacheDir

    lateinit var CARTLIST: MutableList<CartItem>
    lateinit var FAVLIST: MutableList<ShopItem>
    var HOST_URL: String = "http://192.168.149.197:80/"
    var USERTICKETS: MutableSet<Ticket>? = null
    var SHOPTICKETS: MutableSet<Ticket>? = null
    var PROFILE_ID: String? = "roy_YouCurrentlyHaveNoProfileID12345"
    var PROFILE_IMG: String? = null
    var PROFILE_NAME: String? = null
    var PROFILE_EMAIL: String? = null
    var PROFILE_TYPE: String? = null
    var SHOP_ID: String? = null
}
