package com.rexdev.tasty_trends.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rexdev.tasty_trends.adapter.RecyclerViewCartMenuAdapter
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.dataClass.CartItem
import com.rexdev.tasty_trends.global.GlobalVariables
import com.rexdev.tasty_trends.dataClass.CreateTicketReq
import com.rexdev.tasty_trends.domain.RetrofitInstance
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewCartAdapter: RecyclerViewCartMenuAdapter
    private val app = GlobalVariables
    private lateinit var roomAddress: EditText
    private lateinit var btnBuy: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
        roomAddress = findViewById(R.id.tvRoomAddress)
        btnBuy = findViewById(R.id.btnBuyNow)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCartBack = findViewById<ImageView>(R.id.btncartBack)
        btnCartBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        if (app.CARTLIST.isEmpty()) {
            showEmptyCartMessage()
        } else {
            // Initialize RecyclerView
            recyclerView = findViewById(R.id.rvCartList)
            val layoutManager = GridLayoutManager(this, 1)
            recyclerView.layoutManager = layoutManager
            recyclerViewCartAdapter = RecyclerViewCartMenuAdapter()
            recyclerView.adapter = recyclerViewCartAdapter

            loadCartItems()
        }

        btnBuy.setOnClickListener {
            buyAll()
        }
    }

    private fun buyAll() {
        if (app.CARTLIST.isEmpty()) {
            showEmptyCartMessage()
            return
        }

        // Loop through the cart list
        for (index in app.CARTLIST.indices) {
            // Get the current item
            val cartItem = app.CARTLIST[index]

            // Create the ticket request
            val ticketRequest = CreateTicketReq(
                buyer_id = app.PROFILE_ID,
                shop_id = cartItem.shopId,
                item_id = cartItem.itemId,
                quantity = cartItem.quantity,
                price = cartItem.totalPrice(),
                location = roomAddress.text.toString() // User input for location
            )

            lifecycleScope.launch {
                try {
                    // Make the API call to create a ticket
                    val response = RetrofitInstance.api.createTicket(ticketRequest)

                    // Log the response
                    Log.d("API Response", "Response code: ${response.message}, Body: ${response.message}")

                    if (response.success) {
                        // Remove the item from the cart if the ticket is created successfully
                        if (app.CARTLIST.isNotEmpty()) {
                            app.CARTLIST.removeAt(index)
                            recyclerViewCartAdapter.removeItem(index)
                            recyclerViewCartAdapter!!.notifyDataSetChanged()
                            Toast.makeText(this@CartActivity, "Ticket created successfully!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Log the raw error response
                        val errorBody = response.message?: "Unknown error"
                        Log.e("API Error", "Response code: ${response.message}, Body: $errorBody")

                        // Notify the user
                        Toast.makeText(this@CartActivity, "Error creating ticket: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("CartActivity", "Unexpected error: ${e.message}")
                    Log.e("CartActivity", "Full error: ${e.stackTraceToString()}")
                    Toast.makeText(this@CartActivity, "An unexpected error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun buyOne(){
        if (app.CARTLIST.isEmpty()) {
            showEmptyCartMessage()
        } else {
            // Get the first item in the cart
            val firstCartItem = app.CARTLIST[0]
            val ticketRequest = CreateTicketReq(
                buyer_id = app.PROFILE_ID,
                shop_id = firstCartItem.shopId,
                item_id = firstCartItem.itemId,
                quantity = firstCartItem.quantity,
                price = firstCartItem.totalPrice(),
                location = roomAddress.text.toString() // Use the actual user input
            )


            lifecycleScope.launch {
                try {
                    // Make the API call to create a ticket for the first item
                    val response = RetrofitInstance.api.createTicket(ticketRequest)

                    // Log the response
                    Log.e("API Response", "Response code: ${response.message}, Body: ${response.message}")

                    if (response.success) {
                        // Remove the item from the cart if the ticket is created successfully
                        app.CARTLIST.removeAt(0)
                        Toast.makeText(this@CartActivity, "Ticket created successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Log the raw error response
                        val errorBody = response.errorMessage?: "Unknown error"
                        Log.e("API Error", "Response code: ${response.errorMessage}, Body: $errorBody")

                        // Notify the user
                        Toast.makeText(this@CartActivity, "Error creating ticket: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("CartActivity", "Unexpected error: ${e.message}")
                    Log.e("CartActivity", "Full error: ${e.stackTraceToString()}")
                    Toast.makeText(this@CartActivity, "An unexpected error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun loadCartItems() {
        val sharedPreferences = getSharedPreferences("Cart", Context.MODE_PRIVATE)
        val cartItems = app.CARTLIST

        sharedPreferences.all.forEach { entry ->
            if (entry.key.endsWith("_name")) {
                val shopId = entry.key.split("_")[0]
                val itemId = entry.value.toString()
                val itemName = sharedPreferences.getString("${itemId}_name", null)
                val itemImage = sharedPreferences.getString("${itemId}_image", "") ?: ""
                val priceString = sharedPreferences.getString("${itemId}_price", "0") ?: "0"
                val quantity = sharedPreferences.getInt("${itemId}_quantity", 1)

                val pricePerItem = priceString.toDoubleOrNull() ?: 0.0
                val cartItem = CartItem(shopId, itemId, itemName, itemImage, quantity, pricePerItem)
                cartItems.add(cartItem)
            }
        }

        if (cartItems.isEmpty()) {
            showEmptyCartMessage()
        } else {
            recyclerViewCartAdapter.notifyDataSetChanged()
        }
    }

    private fun showEmptyCartMessage() {
        Toast.makeText(this, "No items in the cart", Toast.LENGTH_SHORT).show()
        //findViewById<TextView>(R.id.emptycart).visibility = View.VISIBLE // Optional: Show a view for the empty cart message
    }
}
