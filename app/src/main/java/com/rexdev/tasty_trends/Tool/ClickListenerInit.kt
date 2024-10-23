package com.rexdev.tasty_trends.Tool

import android.content.Context
import androidx.compose.ui.window.application
import com.rexdev.tasty_trends.DataClass.CartItem
import com.rexdev.tasty_trends.DataClass.ShopItem
import com.rexdev.tasty_trends.Global.GlobalVariables
import com.rexdev.tasty_trends.Global.Variables

class ClickListenerInit {
    val app = GlobalVariables

    fun onFavoriteClick(itemFav: ShopItem) {
        app.FAVLIST.add(itemFav)
    }

    fun onCartClick(itemCart: CartItem) {
        if(!app.CARTLIST.contains(itemCart)) {
            app.CARTLIST.add(itemCart)
        }
    }
    fun onRemoveCart(itemRemoveCart: CartItem) {
        app.CARTLIST.remove(itemRemoveCart)
    }

}