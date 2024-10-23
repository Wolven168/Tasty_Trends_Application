package com.migsdev.tastytrends

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rexdev.tasty_trends.Activity.HomeActivity
import com.rexdev.tasty_trends.Adapter.RecyclerViewStallsMenuAdapter
import com.rexdev.tasty_trends.DataClass.ShopItem
import com.rexdev.tasty_trends.R

class FavoritesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewStallsMenuAdapter
    private var favoriteList: MutableList<ShopItem> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorites)

        // Setup edge-to-edge behavior
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Back button behavior
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewFavorites)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Load the list of favorites
        loadFavorites()

        // Initialize and set the adapter
//        adapter = RecyclerViewStallsMenuAdapter(object : OnFavoriteClickListener {
//            override fun onFavoriteClick(item: ShopItem) {
//                // Handle favorite click if needed
//            }
//
//            override fun onCartClick(item: CartItem) {
//                // Handle cart click if needed
//            }
//        }, favoriteList)

        recyclerView.adapter = adapter
    }

    // Function to load favorites from SharedPreferences
    private fun loadFavorites() {
        val sharedPreferences = getSharedPreferences("Favorites", Context.MODE_PRIVATE)

        // Iterate through the shared preferences to retrieve the saved favorites
        sharedPreferences.all.forEach { entry ->
            if (entry.key.endsWith("_name")) {
                val name = entry.value as String
                val price = sharedPreferences.getString("${name}_price", "")
                val image = sharedPreferences.getString("${name}_image", "") // Change to String if using URLs

//                if (price != null && image != null) {
//                    favoriteList.add(ShopItem(name, price, image)) // Adjust constructor if needed
//                }
            }
        }

        // Show message if no favorites are added
        if (favoriteList.isEmpty()) {
            Toast.makeText(this, "No favorites added", Toast.LENGTH_SHORT).show()
        } else {
            adapter.notifyDataSetChanged() // Notify adapter of the data change
        }
    }
}
