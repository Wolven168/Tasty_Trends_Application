package com.rexdev.tasty_trends.tools

import com.rexdev.tasty_trends.dataClass.CartItem
import com.rexdev.tasty_trends.dataClass.ShopItem
import com.rexdev.tasty_trends.global.GlobalVariables

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