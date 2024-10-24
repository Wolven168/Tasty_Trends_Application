package com.rexdev.tasty_trends.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rexdev.tasty_trends.adapter.RecyclerViewCartMenuAdapter
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.dataClass.CartItem
import com.rexdev.tasty_trends.global.GlobalVariables
import com.roydev.tastytrends.CreateTicketReq
import com.roydev.tastytrends.RetrofitInstance
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewCartAdapter: RecyclerViewCartMenuAdapter
    private val app = GlobalVariables

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val roomAddress: EditText? = findViewById(R.id.tvRoomAddress)
        val btnBuy: Button? = findViewById(R.id.btnBuyNow) // Initialize btnBuy after setContentView

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

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
            // Initialize adapter with the correct type
            recyclerView = findViewById(R.id.rvCartList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerViewCartAdapter = RecyclerViewCartMenuAdapter()
            val layoutManager = GridLayoutManager(this, 1)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = recyclerViewCartAdapter

            loadCartItems()
        }

        btnBuy?.setOnClickListener {
            if (app.CARTLIST.isEmpty()) {
                showEmptyCartMessage()
            } else {
                // Prepare the list of ticket requests
                val ticketRequests = app.CARTLIST.map { cartItem ->
                    app.PROFILE_ID?.let { it1 ->
                        CreateTicketReq(
                            buyerId = it1,
                            shopId = cartItem.shopId, // Use the appropriate shopId
                            itemId = cartItem.itemId,
                            quantity = cartItem.quantity,
                            price = cartItem.totalPrice(),
                            location = roomAddress.toString() // You might want to replace this with actual user input
                        )
                    }
                }

                // Launch a coroutine to make the network calls
                lifecycleScope.launch {
                    if (app.CARTLIST.isNotEmpty()) {
                        try {
                            val responses = ticketRequests.map { ticketRequest ->
                                RetrofitInstance.api.createTicket(ticketRequest)
                            }
                            // Check responses
                            responses.forEach { response ->
                                if (response.isSuccessful) {
                                    // Handle success (e.g., show a success message)
                                    Toast.makeText(this@CartActivity, "Ticket created successfully!", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Handle error for this specific ticket request
                                    Toast.makeText(this@CartActivity, "Error creating ticket: ${response.message}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            // Optionally, clear the cart or navigate to another screen
                        } catch (e: Exception) {
                            Toast.makeText(this@CartActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        showEmptyCartMessage()
                    }
                }
            }
        }
    }

    private fun loadCartItems() {
        val sharedPreferences = getSharedPreferences("Cart", Context.MODE_PRIVATE)
        val cartItems = app.CARTLIST // Assuming this is your global variable

        sharedPreferences.all.forEach { entry ->
            if (entry.key.endsWith("_name")) {
                val shopId = entry.key.split("_")[0] // Extract shopId
                val itemId = entry.value.toString()
                val itemName = sharedPreferences.getString("${itemId}_name", null)
                val itemImage = sharedPreferences.getString("${itemId}_image", "") ?: ""
                val priceString = sharedPreferences.getString("${itemId}_price", "0") ?: "0"
                val quantity = sharedPreferences.getInt("${itemId}_quantity", 1)

                // Convert price to Double
                val pricePerItem = priceString.toDoubleOrNull() ?: 0.0

                // Create a new CartItem and add it to the list
                val cartItem = CartItem(shopId, itemId, itemName, itemImage, quantity, pricePerItem)
                cartItems.add(cartItem)
            }
        }

        // Check if cart is empty and show a message
        if (cartItems.isEmpty()) {
            showEmptyCartMessage()
        }
        else {
            recyclerViewCartAdapter!!.notifyDataSetChanged()
        }
    }



    private fun showEmptyCartMessage() {
        // Here you can use a TextView to display the message
        Toast.makeText(this, "No items in the cart", Toast.LENGTH_SHORT).show()
    }
}
