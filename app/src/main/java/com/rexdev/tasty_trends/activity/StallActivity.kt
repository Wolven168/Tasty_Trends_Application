package com.rexdev.tasty_trends.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rexdev.tasty_trends.adapter.RecyclerViewStallsMenuAdapter
import com.rexdev.tasty_trends.dataClass.ShopItem
import com.rexdev.tasty_trends.dataClass.Stalls
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.dataClass.getAvailability
import com.rexdev.tasty_trends.domain.RetrofitInstance
import com.rexdev.tasty_trends.global.GlobalVariables
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class StallActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewStallMenuAdapter: RecyclerViewStallsMenuAdapter
    private var shopItemList = mutableListOf<ShopItem>()
    private var shopId: String? = null
    private lateinit var stallName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stallactivity)

        val shopData = intent.getParcelableExtra<Stalls>("stall_data")
        shopData?.let {
            shopId = it.shop_id
            stallName = findViewById(R.id.StallMenuName)
            stallName.text = "${it.shop_name} Menu"
        }

        setupEdgeToEdge()
        setupRecyclerView()
        loadSampleItemData()
        loadStallItemsData()
        setupButtons()
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rvStallLists)
        recyclerViewStallMenuAdapter = RecyclerViewStallsMenuAdapter(shopItemList)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = recyclerViewStallMenuAdapter
    }

    private fun setupButtons() {
        findViewById<ImageView>(R.id.btncart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        findViewById<ImageView>(R.id.btnback).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun loadSampleItemData() {
        val sampleItems = listOf(
            ShopItem("Egg_Silog_123456", "jfc_123456", "Egg Silog", 50.00, "https://example.com/image1.jpg", true),
            ShopItem("Chicken_Barbeque_123456", "jfc_123456", "Chicken Barbeque", 75.00, "https://example.com/image2.jpg", true)
        )

        shopItemList.addAll(sampleItems)
        recyclerViewStallMenuAdapter.notifyDataSetChanged()
    }

    private fun loadStallItemsData() {
        shopId ?: run {
            Toast.makeText(this, "Shop ID is missing", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getShopItems(shopId!!)
                Log.d("StallActivity", "API Response: $response")

                if (response.success) {
                    withContext(Dispatchers.Main) {
                        shopItemList.clear()
                        response.items?.map { item ->
                            ShopItem(
                                item_id = item.item_id,
                                shop_id = item.shop_id,
                                item_name = item.item_name,
                                item_price = item.item_price,
                                item_image = item.item_image,
                                available = item.getAvailability()
                            )
                        }?.let { shopItemList.addAll(it) }
                        recyclerViewStallMenuAdapter.notifyDataSetChanged()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showError(response.message ?: "Unknown error")
                    }
                }
            } catch (e: HttpException) {
                Log.e("StallActivity", "HTTP Error: ${e.message()}")
                withContext(Dispatchers.Main) {
                    showError("Network error: ${e.message()}")
                }
            } catch (e: Exception) {
                Log.e("StallActivity", "Error: ${e.message}")
                withContext(Dispatchers.Main) {
                    showError("Error: ${e.message}")
                }
            }
        }
    }

    private suspend fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
