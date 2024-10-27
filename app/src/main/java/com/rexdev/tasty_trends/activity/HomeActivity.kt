package com.rexdev.tasty_trends.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.rexdev.tasty_trends.adapter.RecyclerViewStallsAdapter
import com.rexdev.tasty_trends.dataClass.Stalls
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.global.GlobalVariables
import com.rexdev.tasty_trends.domain.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewStallsAdapter: RecyclerViewStallsAdapter
    private var stallList = mutableListOf<Stalls>()
    private val app = GlobalVariables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_drawer_navigation)

        // Apply system window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cartIcon = findViewById<ImageButton>(R.id.cartIcon)
        cartIcon.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.rvStallLists)
        recyclerViewStallsAdapter = RecyclerViewStallsAdapter(this, stallList)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = recyclerViewStallsAdapter

        recyclerViewStallsAdapter.onItemClick = { stallData ->
            val intent = Intent(this, StallActivity::class.java)
            intent.putExtra("stall_data", stallData)
            startActivity(intent)
        }

        loadStalls() // Load stalls from database

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)

        // Set up the Toolbar and hide the title
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Hides the toolbar title

        // Set up NavigationView and listen for item selections
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Set up drawer toggle
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun loadStalls() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getAllShops() // Replace with your actual API call
                if (response.success == true) {
                    stallList.clear() // Clear the existing list
                    response.shops?.let { stallList.addAll(it) } // Add new stalls to the list

                    withContext(Dispatchers.Main) {
                        recyclerViewStallsAdapter.notifyDataSetChanged() // Notify the adapter about data changes
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(recyclerView, response.errorMessage ?: "Failed to load stalls", Snackbar.LENGTH_LONG).show()
                        loadTestStallsData() // Load sample data
                    }
                }
            } catch (e: Exception) {
                // If fetching fails, load the sample data
                withContext(Dispatchers.Main) {
                    loadTestStallsData() // Load sample data
                    Snackbar.make(recyclerView, "Failed to load stalls, showing sample data.", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadTestStallsData() {
        stallList.add(Stalls("jfc_12345678901234567890123456789012", "jfc",
            R.drawable.jfc.toString()
        ))
        stallList.add(Stalls("arnold_12345678901234567890123456789012", "arnold",
            R.drawable.arnold.toString()
        ))
        recyclerViewStallsAdapter.notifyDataSetChanged()
    }

    // Handle navigation item selection
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navHome -> finish() // Prevents creating a new instance
            R.id.navProfile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.navFavorite -> startActivity(Intent(this, FavoritesActivity::class.java))
            R.id.navOrders -> startActivity(Intent(this, OrderActivity::class.java))
        }
        // Close the drawer after selecting an item
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // Handle back button behavior
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
