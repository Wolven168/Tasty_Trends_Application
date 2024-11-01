package com.rexdev.tasty_trends.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.dataClass.GetShops
import com.rexdev.tasty_trends.domain.RetrofitInstance
import com.rexdev.tasty_trends.global.GlobalVariables
import kotlinx.coroutines.launch

class StallOwnerMenuActivity : AppCompatActivity() {
    private val app = GlobalVariables

    // Back
    private lateinit var backBtn: ImageView
    // Shop details
    private lateinit var shopImage: ImageView
    private lateinit var shopName: TextView
    private lateinit var shopEmail: TextView
    // Options
    private lateinit var shopHome: TextView
    private lateinit var shopProfile: TextView
    private lateinit var shopOrders: TextView
    private lateinit var shopLogout: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stall_owner_menu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        backBtn = findViewById(R.id.ivBack)
        shopImage = findViewById(R.id.ivShopImage)
        shopName = findViewById(R.id.tvShopName)
        shopEmail = findViewById(R.id.tvShopEmail)
        shopHome = findViewById(R.id.tvShopHomeBtn)
        shopProfile = findViewById(R.id.tvShopProfileBtn)
        shopOrders = findViewById(R.id.tvShopOrdersBtn)
        shopLogout = findViewById(R.id.tvShopLogout)

        backBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        shopHome.setOnClickListener {
            startActivity(Intent(this, StallOwnerActivity::class.java))
        }
        shopProfile.setOnClickListener {
            startActivity(Intent(this, StallOwnerProfileActivity::class.java))
        }
        shopOrders.setOnClickListener {
            startActivity(Intent(this, StallOwnerOrdersActivity::class.java))
        }
        shopLogout.setOnClickListener {
            app.logout(this)
        }
        showSnackbar("Error loading shop details")

//        loadShopDetails() // Load shop details when the activity starts
    }

    private fun loadShopDetails() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getAllShops()
                handleResponse(response)
            } catch (e: Exception) {
                handleLoadFailure(e)
            }
        }
    }

    private suspend fun handleResponse(response: GetShops) {
        if (response.success == true) {
        } else {
            showSnackbar(response.errorMessage ?: "Failed to load shop")
        }
    }

    private fun handleLoadFailure(e: Exception) {
        showSnackbar("Error loading shop details: ${e.message}")
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG).show()
    }
}
