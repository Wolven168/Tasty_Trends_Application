package com.rexdev.tasty_trends.global

import com.rexdev.tasty_trends.dataClass.CartItem
import com.rexdev.tasty_trends.dataClass.ShopItem

object GlobalVariables {
    lateinit var CARTLIST : MutableList<CartItem>
    lateinit var FAVLIST : MutableList<ShopItem>
    var PROFILE_ID : String = "roy_YouCurrentlyHaveNoProfileID12345"
    var PROFILE_IMG : String? = null
    var PROFILE_NAME : String? = null
}