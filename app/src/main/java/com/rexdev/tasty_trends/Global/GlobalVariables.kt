package com.rexdev.tasty_trends.Global

import com.rexdev.tasty_trends.DataClass.CartItem
import com.rexdev.tasty_trends.DataClass.ShopItem

object GlobalVariables {
    lateinit var CARTLIST : MutableList<CartItem>
    lateinit var FAVLIST : MutableList<ShopItem>
    var PROFILE_ID : String? = null
    var PROFILE_IMG : String? = null
    var PROFILE_NAME : String? = null
}