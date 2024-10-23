package com.rexdev.tasty_trends.Activity

import android.annotation.SuppressLint
import android.app.Application
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rexdev.tasty_trends.Adapter.RecyclerViewCartMenuAdapter
import com.rexdev.tasty_trends.Adapter.RecyclerViewStallsMenuAdapter
import com.rexdev.tasty_trends.DataClass.CartItem
import com.rexdev.tasty_trends.DataClass.ShopItem
import com.rexdev.tasty_trends.Global.GlobalVariables
import com.rexdev.tasty_trends.Global.Variables
import com.rexdev.tasty_trends.R

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewCartAdapter: RecyclerViewCartMenuAdapter
    private val app = GlobalVariables

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val roomAddress = findViewById<EditText>(R.id.tvRoomAddress)
        val btnBuy = findViewById<Button>(R.id.btnBuyNow)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btncartBack = findViewById<ImageView>(R.id.btncartBack)
        btncartBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Initialize adapter with the correct type
        recyclerView = findViewById(R.id.rvCartList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewCartAdapter = RecyclerViewCartMenuAdapter()
        val layoutManager = GridLayoutManager(this, 1)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = recyclerViewCartAdapter


        // Load cart items
        loadCartItems()
    }

    private fun loadCartItems() {
        val sharedPreferences = getSharedPreferences("Cart", Context.MODE_PRIVATE)
        sharedPreferences.all.forEach { entry ->
            if (entry.key.endsWith("_name")) {
                val itemId = entry.value.toString()
                val itemName = sharedPreferences.getString("${itemId}_name", null)
                val itemImage = sharedPreferences.getString("${itemId}_image", null)
                val priceString = sharedPreferences.getString("${itemId}_price", "0") ?: "0"
                val quantity = sharedPreferences.getInt("${itemId}_quantity", 1) // Assuming you save quantity

                // Ensure price is convertible to a number if needed
                val pricePerItem = priceString.toDoubleOrNull() ?: 0.0

            }
        }
    }

    private fun showEmptyCartMessage() {
        // Here you can use a TextView to display the message
        Toast.makeText(this, "No items in the cart", Toast.LENGTH_SHORT).show()
    }
}
