package com.rexdev.tasty_trends.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class RegisterReq(
    val user_name: String,
    val email: String,
    val password: String,
)

data class RegisterRes(
    val message: String? = null,
    val success: Boolean
)

data class LoginReq(
    val email: String,
    val password: String
)

data class LoginRes(
    val token: String? = null, // Nullable in case the token is not provided
    val message: String? = null,
    val success: Boolean,
    val id: String? = null,
)

data class UserDataReq(
    val id: String
)

data class UserDataRes(
    val img: String,
    val name: String,
    val shopId: String,
)

data class TicketReq(
    val shop_id: String, // ID of the shop where the ticket is for
    val buyer_id: String  // ID of the buyer
)

data class Ticket(
    val id: String,      // Unique identifier for the ticket
    val shop_id: String,  // ID of the shop associated with the ticket
    val buyer_id: String, // ID of the buyer who purchased the ticket
    val status: String,
)

data class CreateTicketReq(
    val buyer_id: String,
    val shop_id: String,
    val item_id: String,
    val quantity: Int,
    val price: Double,
    val location: String?
)

@Parcelize
data class CreateTicketRes (
    val message: String? = null,
    val success: Boolean,
    val errorMessage: String? = null, // Optional field for error messages
) : Parcelable

data class TicketRes(
    val message: String? = null        // Additional message from the response
)
