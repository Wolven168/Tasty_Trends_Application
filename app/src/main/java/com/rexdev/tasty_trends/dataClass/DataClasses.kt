package com.rexdev.tasty_trends.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class CartItem(
    val shop_id: String,
    val item_id: String,
    val item_name: String?,
    val item_image: String?,
    var quantity: Int,
    val pricePerItem: Double // Adding price for calculation
) {
    fun totalPrice(): Double {
        return quantity * pricePerItem
    }
}

data class ShopItem(
    val item_id: String,     // Unique identifier for the shop item
    val shop_id: String,     // Unique identifier for the shop selling the item
    val item_name: String,   // Name of the shop item
    val item_price: Double,      // Price of the item (considering decimals)
    val item_image: String?,      // URL or resource ID for the item's image
    val available: Boolean,  // Availability status of the item
    // val description: String? // Optional description of the item
)fun ShopItem.formattedPrice(): String {
    return String.format("₱%.2f", item_price)
}

data class ShopItemData(
    val id: Int, // Ensure this type matches your API response
    val item_id: String,
    val shop_id: String,
    val item_name: String,
    val item_price: Double,
    val item_image: String?,
    val available: Int, // Assuming this is an Int based on your API response
    val item_desc: String?, // Nullable
    val created_at: String,
    val updated_at: String
)
fun ShopItemData.getAvailability(): Boolean {
    return when (available) {
        1 -> true
        0 -> false
        else -> throw IllegalArgumentException("Invalid availability value: $available")
    }
}


@Parcelize
data class Stalls(
    val shop_id: String,         // Unique identifier for the shop (stall)
    val shop_name: String,       // Name of the shop (stall)
    val shop_image: String,           // URL or resource ID for the shop's image
) : Parcelable

data class DetailedStalls(
    val shop_id: String,         // Unique identifier for the shop (stall)
    val shop_owner_id: String,
    val shop_name: String,       // Name of the shop (stall)
    val shop_image: String,           // URL or resource ID for the shop's image
)

data class Ticket(
    val ticket_id: String,
    val buyer_id: String,
    val buyer_name: String?,
    val shop_id: String,
    val shop_name: String?,
    val item_id: String,
    val item_name: String?,
    val item_image: String?,
    val quantity: Int,
    val price: Double,
    val status: String,
    val location: String?,
)

@Parcelize
data class RefinedTicket(
    val ticket_id: String,      // Unique identifier for the ticket
    val shop_id: String,  // ID of the shop associated with the ticket
    val shop_name: String?,
    val buyer_id: String, // ID of the buyer who purchased the ticket
    val buyer_name: String?,
    val item_id: String,
    val item_name: String?,
    val item_image: String?,
    val quantity: Int,
    val price: Double,
    var status: String,
    val location: String?,
) : Parcelable