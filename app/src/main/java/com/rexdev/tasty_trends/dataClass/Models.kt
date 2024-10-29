package com.rexdev.tasty_trends.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// ============== REQUESTS ==============

data class RegisterReq(
    val user_name: String,
    val email: String,
    val password: String,
)

data class LoginReq(
    val email: String,
    val password: String
)

data class TicketReq(
    val shop_id: String, // ID of the shop where the ticket is for
    val buyer_id: String  // ID of the buyer
)

data class CreateTicketReq(
    val buyer_id: String,
    val shop_id: String,
    val item_id: String,
    val quantity: Int,
    val price: Double,
    val location: String?
)

data class UpdateUser(
    val user_name: String?,
    val email: String?,
    val password: String?,
    val shop_id: String?,
    val user_image: String?,
    val phone_num: String?,
    val student_num: String?,
    val favorites: List<ShopItem>?,
)

data class UpdateFavorites(
    val favorites: List<ShopItem>?
)

data class ForgotPass(
    val email: String
)

// ============== RESPONSES ==============

data class GenericResponse (
    val message: String?,
    val success: Boolean?,
    val errorMessage: String? // Optional field for error messages
)

data class LoginRes(
    val token: String?, // Nullable in case the token is not provided
    val message: String?,
    val success: Boolean,
    val userDetails: UserDetails?,
    val errorMessage: String?,
)

data class ShopItemsReq(
    val message: String?,
    val success: Boolean,
    val items: List<ShopItemData>?,
    val errorMessage: String?,
)

data class GetShops(
    val message: String?,
    val success: Boolean,
    val shops: List<Stalls>?,
    val errorMessage: String?,
)

data class GetTickets(
    val message: String?,
    val success: Boolean,
    val tickets: List<RefinedTicket>?
)

data class GetUserTicketData(
    val message: String?,
    val success: Boolean,
    val user_name: String?,
)

data class GetShopTicketData(
    val message: String?,
    val success: Boolean,
    val shop_name: String?,
)

data class GetItemTicketData(
    val message: String?,
    val success: Boolean,
    val item_name: String?,
    val item_image: String,
)

data class GetTicketData(
    val message: String?,
    val success: Boolean,
    val tickets: Ticket,
    val errorMessage: String?,
)

data class GetShopDetails(
    val shop_id: String?,
    val shop_name: String?,
    val shop_email: String?,
    val shop_image: String?,
)