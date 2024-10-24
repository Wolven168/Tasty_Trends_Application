package com.roydev.tastytrends

import com.rexdev.tasty_trends.dataClass.ShopItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @Headers("Content-Type: application/json")

    @POST("tasters/register")
    suspend fun register(@Body registerReq: RegisterReq): Response<RegisterRes>

    @POST("tasters/login")
    suspend fun login(@Body loginReq: LoginReq): LoginRes

    @GET("tickets/{id}")
    suspend fun getTicket(@Path("id") ticketId: String): TicketRes

    @POST("tickets/create")
    suspend fun createTicket(@Body createTicketReq: CreateTicketReq?): TicketRes

    @GET("items/indexShopItems/{id}")
    suspend fun getShopItems(@Path("id") s: String): Response<List<ShopItem>>







    /*
    // Comment out until I add DeleteRes and UpdateTicketREs
    // Example of a DELETE request
    @DELETE("tickets/{id}")
    suspend fun deleteTicket(@Path("id") ticketId: String): DeleteRes

    // Example of a PUT request
    @PUT("tickets/{id}")
    suspend fun updateTicket(@Path("id") ticketId: String, @Body request: UpdateTicketReq): UpdateTicketRes
     */
}
