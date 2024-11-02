package com.roydev.tastytrends

import com.rexdev.tasty_trends.dataClass.CreateTicketReq
import com.rexdev.tasty_trends.dataClass.ForgotPass
import com.rexdev.tasty_trends.dataClass.GenericResponse
import com.rexdev.tasty_trends.dataClass.GetItemTicketData
import com.rexdev.tasty_trends.dataClass.GetShopTicketData
import com.rexdev.tasty_trends.dataClass.GetShops
import com.rexdev.tasty_trends.dataClass.GetTickets
import com.rexdev.tasty_trends.dataClass.GetUserTicketData
import com.rexdev.tasty_trends.dataClass.LoginReq
import com.rexdev.tasty_trends.dataClass.LoginRes
import com.rexdev.tasty_trends.dataClass.RegisterReq
import com.rexdev.tasty_trends.dataClass.ShopItemsReq
import com.rexdev.tasty_trends.dataClass.UpdateTicketStatus
import com.rexdev.tasty_trends.dataClass.UpdateUser
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @Headers("Content-Type: application/json")

    // ================ USER CONTROLS ================ //

    // Register a new user
    @POST("api/tasters/register")
    suspend fun register(@Body registerReq: RegisterReq): GenericResponse

    // Log in an existing user
    @POST("api/tasters/login")
    suspend fun login(@Body loginReq: LoginReq): LoginRes

    @PUT("api/tasters/update/{user_id}")
    suspend fun updateUser(@Path("user_id") user_id: String, @Body updateUser: UpdateUser): GenericResponse

    @GET("api/tasters/getUserName/{buyer_id}")
    suspend fun getBuyerTicketData(@Path("buyer_id") buyer_id: String): GetUserTicketData

    @POST("api/forgot-password")
    suspend fun forgotPass(@Body email: ForgotPass): GenericResponse

    // ================ SHOP/STALL/ITEM CONTROLS ================ //
    @GET("api/shops/indexAllShops")
    suspend fun getAllShops(): GetShops

    @GET("api/shops/indexShopItems/{shop_id}")
    suspend fun getShopTicketData(@Path("shop_id") shop_id: String): GetShopTicketData

    // Get items associated with a specific shop
    @GET("api/items/indexShopItems/{shop_id}")
    suspend fun getShopItems(@Path("shop_id") shop_id: String): ShopItemsReq

    @GET("api/items/show/TicketData/{item_id}")
    suspend fun getItemTicketData(@Path("item_id") item_id: String): GetItemTicketData

    // ================ TICKET CONTROLS ================ //

    // Retrieve a specific ticket by ID
    @GET("api/tickets/userTickets/{buyer_id}")
    suspend fun getUserTickets(@Path("buyer_id") buyer_id: String): GetTickets

    @GET("api/tickets/shopTickets/{shop_id}")
    suspend fun getShopTickets(@Path("shop_id") shop_id: String): GetTickets

    // Create a new ticket
    @POST("api/tickets/create")
    suspend fun createTicket(@Body createTicketReq: CreateTicketReq): GenericResponse

    @DELETE("api/tickets/delete/{ticket_id}")
    suspend fun deleteTicket(@Path("ticket_id") ticket_id: String): GenericResponse

    @POST("api/tickets/status/{ticket_id}")
    suspend fun updateTicketStatus(@Path("ticket_id") ticket_id: String, @Body status: UpdateTicketStatus): GenericResponse

    //
    @Multipart
    @POST("3/image")
    fun uploadImage(
        @Header("Authorization") authHeader: String,
        @Part image: MultipartBody.Part
    ): Call<ResponseBody>
}
