package com.roydev.tastytrends

import com.rexdev.tasty_trends.dataClass.CreateTicketReq
import com.rexdev.tasty_trends.dataClass.CreateTicketRes
import com.rexdev.tasty_trends.dataClass.LoginReq
import com.rexdev.tasty_trends.dataClass.LoginRes
import com.rexdev.tasty_trends.dataClass.RegisterReq
import com.rexdev.tasty_trends.dataClass.RegisterRes
import com.rexdev.tasty_trends.dataClass.ShopItem
import com.rexdev.tasty_trends.dataClass.TicketRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @Headers("Content-Type: application/json")

    // Register a new user
    @POST("tasters/register")
    suspend fun register(@Body registerReq: RegisterReq): Response<RegisterRes>

    // Log in an existing user
    @POST("tasters/login")
    suspend fun login(@Body loginReq: LoginReq): LoginRes

    // Retrieve a specific ticket by ID
    @GET("tickets/{id}")
    suspend fun getTicket(@Path("id") ticketId: String): TicketRes

    // Create a new ticket
    @POST("tickets/create")
    suspend fun createTicket(@Body createTicketReq: CreateTicketReq): CreateTicketRes

    // Get items associated with a specific shop
    @GET("items/indexShopItems/{id}")
    suspend fun getShopItems(@Path("id") shopId: String): List<ShopItem>

    /*
    // Uncomment when DeleteRes and UpdateTicketRes are implemented
    @DELETE("tickets/{id}")
    suspend fun deleteTicket(@Path("id") ticketId: String): Response<DeleteRes>

    @PUT("tickets/{id}")
    suspend fun updateTicket(@Path("id") ticketId: String, @Body request: UpdateTicketReq): Response<UpdateTicketRes>
    */
}
