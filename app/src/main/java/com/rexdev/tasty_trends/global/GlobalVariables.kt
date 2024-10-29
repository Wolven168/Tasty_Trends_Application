package com.rexdev.tasty_trends.global

import android.content.Context
import com.rexdev.tasty_trends.dataClass.CartItem
import com.rexdev.tasty_trends.dataClass.ShopItem
import com.rexdev.tasty_trends.dataClass.Ticket
import java.io.File

object GlobalVariables {
    // Hold the application context
    private lateinit var appContext: Context

    // Initialize the app context
    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    val cacheDir: File
        get() = appContext.cacheDir // Return the app's cache directory

    lateinit var CARTLIST: MutableList<CartItem>
    lateinit var FAVLIST: MutableList<ShopItem>
    var HOST_URL: String = "http://192.168.149.197:80/"
    var USERTICKETS: MutableSet<Ticket>? = null
    var SHOPTICKETS: MutableSet<Ticket>? = null
    var PROFILE_ID: String = "roy_YouCurrentlyHaveNoProfileID12345"
    var PROFILE_IMG: String? = null
    var PROFILE_NAME: String? = null
    var SHOP_ID: String? = null
}
