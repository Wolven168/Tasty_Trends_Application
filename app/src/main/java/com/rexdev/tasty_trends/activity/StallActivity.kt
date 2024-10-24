package com.rexdev.tasty_trends.activity

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
import com.rexdev.tasty_trends.adapter.RecyclerViewStallsMenuAdapter
import com.rexdev.tasty_trends.dataClass.ShopItem
import com.rexdev.tasty_trends.dataClass.Stalls
import com.rexdev.tasty_trends.R
import com.roydev.tastytrends.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class StallActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var recyclerViewStallMenuAdapter: RecyclerViewStallsMenuAdapter? = null
    private var shopItemList = mutableListOf<ShopItem>()
    private var shop_Id: String? = ""
    private var shop_Name: String? = null
    private var shop_Img: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stallactivity)
        val shopData = intent.getParcelableExtra<Stalls>("stall_data")

        shopData?.let {
            shop_Id = shopData.shopId
            shop_Name = shopData.shopName
            shop_Img = shopData.image
        }

        // Setup edge-to-edge behavior
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCart = findViewById<ImageView>(R.id.btncart)
        btnCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        val btnBack = findViewById<ImageView>(R.id.btnback)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.rvStallLists)
        recyclerViewStallMenuAdapter = RecyclerViewStallsMenuAdapter(shopItemList)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = recyclerViewStallMenuAdapter

        loadSampleItemData()
        //loadStallItemsData()
    }

    private fun loadSampleItemData() {
        // Create sample items
        val sampleItems = listOf(
            ShopItem("Egg_Silog_12345678901234567890123456789012",
                "jfc_12345678901234567890123456789012",
                "Egg_Silog",
                50.00,
                "https://gluttodigest.com/wp-content/uploads/2018/03/beef-tapsilog-silog-what-is-how-to-make-and-where-find-buy-order-best-longsilog-bistro-express-tosilog-bangsilog-spamsilog-menu-recipe-meals-restaurants-near-me-500x363.jpg",
                true),
            ShopItem("Chicken_Barbeque_12345678901234567890123456789012",
                "jfc_12345678901234567890123456789012",
                "Chicken Barbeque",
                75.00,
                "https://www.allrecipes.com/thmb/APtZNY1GgOf3Ph0JUc-j4dImjrU=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/2467480-southern-bbq-chicken-Allrecipes-Magazine-4x3-1-3e180dccbaae446c8c2d05f708611fc6.jpg",
                true),
        )

        // Clear the list and add sample items
        shopItemList.clear()
        shopItemList.addAll(sampleItems)

        // Notify the adapter about data changes
        recyclerViewStallMenuAdapter!!.notifyDataSetChanged()
    }

    private fun loadStallItemsData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = shop_Id?.let { RetrofitInstance.api.getShopItems(it) }
                if (response != null) {
                    if (response.isSuccessful) {
                        response.body()?.let { items ->
                            shopItemList.clear()
                            shopItemList.addAll(items)
                            withContext(Dispatchers.Main) {
                                recyclerViewStallMenuAdapter!!.notifyDataSetChanged()
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@StallActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@StallActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@StallActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // Notify the adapter about data changes
        recyclerViewStallMenuAdapter!!.notifyDataSetChanged()
    }
}
