package com.roydev.tastytrends

data class RegisterReq(
    val username: String,
    val email: String,
    val password: String,
)

data class RegisterRes(
    val message: String,
    val isSuccessful: Boolean
)

data class LoginReq(
    val email: String,
    val password: String
)

data class LoginRes(
    //val token: String, // The authentication token
    val message: String,
    val success: Boolean,
    val id: String,
)

data class UserDataReq(
    val id: String
)

data class UserDataRes(
    val img: String,
    val name: String,
    val shop_id: String,
)

data class TicketReq(
    val shopId: String, // ID of the shop where the ticket is for
    val buyerId: String  // ID of the buyer
)

data class Ticket(
    val id: String,      // Unique identifier for the ticket
    val shopId: String,  // ID of the shop associated with the ticket
    val buyerId: String, // ID of the buyer who purchased the ticket
    val status: String,
)

data class CreateTicketReq (
    val buyerId: String,
    val shopId: String,
    val itemId: String,
    val quantity: Int,
    val price: Double,
    val location: String,
)

data class TicketRes(
    val tickets: List<Ticket>, // List of tickets associated with the response
    val message: String        // Additional message from the response
) {
    val isSuccessful: Boolean
        get() {
            TODO()
        }
}
