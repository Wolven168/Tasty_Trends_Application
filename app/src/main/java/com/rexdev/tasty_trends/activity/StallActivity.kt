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
    private var shopItemList: MutableList<ShopItem> = mutableListOf() // Initialize the list
    private var shop_id: String? = null
    private lateinit var stall_name: TextView
    private val app = GlobalVariables
    private var varName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stallactivity)

        val shopData = intent.getParcelableExtra<Stalls>("stall_data") // Initializing parsed data
        shopData?.let {
            shop_id = it.shop_id
            varName = it.shop_name
        }

        stall_name = findViewById(R.id.StallMenuName)
        stall_name.text = varName + " Menu"

        // Setup edge-to-edge behavior
        setupEdgeToEdge()

        // Setup RecyclerView
        setupRecyclerView()

        // Load sample data
        loadSampleItemData()

        // Load stall items from API
        loadStallItemsData()

        // Set up buttons
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
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
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
        // Create sample items
        val sampleItems = listOf(
            ShopItem("Egg_Silog_123456",
                "jfc_123456",
                "Egg Silog",
                50.00,
                "https://gluttodigest.com/wp-content/uploads/2018/03/beef-tapsilog-silog-what-is-how-to-make-and-where-find-buy-order-best-longsilog-bistro-express-tosilog-bangsilog-spamsilog-menu-recipe-meals-restaurants-near-me-500x363.jpg",
                true,
                ),
            ShopItem("Chicken_Barbeque_123456",
                "jfc_123456",
                "Chicken Barbeque",
                75.00,
                "https://www.allrecipes.com/thmb/APtZNY1GgOf3Ph0JUc-j4dImjrU=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/2467480-southern-bbq-chicken-Allrecipes-Magazine-4x3-1-3e180dccbaae446c8c2d05f708611fc6.jpg",
                true,
                )
        )

        // Add sample items to the list
        shopItemList.addAll(sampleItems)

        // Notify the adapter about data changes
        recyclerViewStallMenuAdapter.notifyDataSetChanged()
    }

    private fun loadStallItemsData() {
        if (shop_id == null) {
            Toast.makeText(this, "Shop ID is missing", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getShopItems(shop_id!!)
                Log.d("StallActivity", "API Response: $response") // Log the response

                if (response.success) {
                    withContext(Dispatchers.Main) {
                        shopItemList.clear() // Clear previous items
                        response.items?.let { items ->
                            // Convert each item into a ShopItem instance
                            items.forEach { item ->
                                // Create a ShopItem from each response item
                                val shopItem = ShopItem(
                                    item_id = item.item_id,
                                    shop_id = item.shop_id,
                                    item_name = item.item_name,
                                    item_price = item.item_price,
                                    item_image = item.item_image,
                                    available = item.getAvailability(),
                                )
                                shopItemList.add(shopItem)
                            }
                            recyclerViewStallMenuAdapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@StallActivity, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: HttpException) {
                Log.e("StallActivity", "HTTP Error: ${e.message()}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@StallActivity, "Network error: ${e.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("StallActivity", "Error: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@StallActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
