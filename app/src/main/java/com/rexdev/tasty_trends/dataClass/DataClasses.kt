package com.rexdev.tasty_trends.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class CartItem(
    val shopId: String,
    val itemId: String,
    val itemName: String?,
    val itemImage: String,
    var quantity: Int,
    val pricePerItem: Double // Adding price for calculation
) {
    fun totalPrice(): Double {
        return quantity * pricePerItem
    }
}


data class ShopItem(
    val itemId: String,     // Unique identifier for the shop item
    val shopId: String,     // Unique identifier for the shop selling the item
    val itemName: String,   // Name of the shop item
    val price: Double,      // Price of the item (considering decimals)
    val image: String,      // URL or resource ID for the item's image
    val available: Boolean,  // Availability status of the item
    // val description: String? // Optional description of the item
)fun ShopItem.formattedPrice(): String {
    return String.format("₱%.2f", price)
}

@Parcelize
data class Stalls(
    val shopId: String,         // Unique identifier for the shop (stall)
    val shopName: String,       // Name of the shop (stall)
    val image: String,           // URL or resource ID for the shop's image
) : Parcelable
