package com.rexdev.tasty_trends.activity

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
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.adapter.RecyclerViewStallsMenuAdapter
import com.rexdev.tasty_trends.global.GlobalVariables

class FavoritesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewStallsMenuAdapter
    private val favoriteList = GlobalVariables.FAVLIST // Directly use the global favorite list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorites)

        // Setup edge-to-edge behavior
        setupEdgeToEdge()

        // Back button behavior
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewFavorites)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Initialize and set the adapter with the global favorite list
        adapter = RecyclerViewStallsMenuAdapter(favoriteList)
        recyclerView.adapter = adapter

        // Show message if no favorites are added
        if (favoriteList.isEmpty()) {
            Toast.makeText(this, "No favorites added", Toast.LENGTH_SHORT).show()
        }
    }

    // Setup edge-to-edge behavior
    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
