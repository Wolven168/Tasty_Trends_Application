package com.rexdev.tasty_trends.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.adapter.RecyclerViewStallOwnerOrdersAdapter
import com.rexdev.tasty_trends.dataClass.RefinedTicket
import com.rexdev.tasty_trends.domain.RetrofitInstance
import com.rexdev.tasty_trends.global.GlobalVariables
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Color
import retrofit2.HttpException

class StallOwnerOrdersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewStallOwnerOrdersAdapter: RecyclerViewStallOwnerOrdersAdapter
    private var ticketList: MutableList<RefinedTicket> = mutableListOf() // Initialize as an empty list
    private var filteredList: MutableList<RefinedTicket> = mutableListOf() // Initialize as an empty list
    private var filter: String? = null
    private val app = GlobalVariables
    private lateinit var btn_Back: ImageView
    private lateinit var btn_All: TextView
    private lateinit var btn_Pending: TextView
    private lateinit var btn_Accepted: TextView
    private lateinit var btn_Cancelled: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stall_owner_orders)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_Back = findViewById(R.id.btn_SOOBack)
        btn_All = findViewById(R.id.btn_All)
        btn_Pending = findViewById(R.id.btn_Pending)
        btn_Cancelled = findViewById(R.id.btn_cancelled)
        btn_Accepted = findViewById(R.id.btn_accepted)

        setupRecyclerView()
        loadShopTickets()

        btn_Back.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        btn_All.setOnClickListener {
            filter = null
            updateButtonColors(btn_All)
            loadFilteredTickets(filter) // Load all tickets
        }

        btn_Pending.setOnClickListener {
            filter = "Pending"
            updateButtonColors(btn_Pending)
            loadFilteredTickets(filter)
        }

        btn_Accepted.setOnClickListener {
            filter = "Accepted"
            updateButtonColors(btn_Accepted)
            loadFilteredTickets(filter)
        }

        btn_Cancelled.setOnClickListener {
            filter = "Cancelled"
            updateButtonColors(btn_Cancelled)
            loadFilteredTickets(filter)
        }
    }

    private fun updateButtonColors(selectedButton: TextView) {
        btn_All.setTextColor(Color.BLACK)
        btn_Pending.setTextColor(Color.BLACK)
        btn_Accepted.setTextColor(Color.BLACK)
        btn_Cancelled.setTextColor(Color.BLACK)
        selectedButton.setTextColor(Color.GREEN)
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rvStallOwnerOrders)
        recyclerViewStallOwnerOrdersAdapter = RecyclerViewStallOwnerOrdersAdapter(filteredList)
        val layoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerViewStallOwnerOrdersAdapter
    }

    private fun loadShopTickets() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getShopTickets(app.SHOP_ID!!)
                if (response.success) {
                    ticketList.clear() // Clear existing tickets
                    response.tickets?.let { tickets ->
                        ticketList.addAll(tickets)
                    }
                    // Call loadFilteredTickets after loading user tickets
                    withContext(Dispatchers.Main) {
                        loadFilteredTickets(filter) // Update the filtered tickets
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@StallOwnerOrdersActivity, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@StallOwnerOrdersActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@StallOwnerOrdersActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadFilteredTickets(filter: String?) {
        filteredList.clear() // Clear the current filtered list

        if (filter != null) {
            ticketList.forEach { ticket ->
                if (ticket.status == filter) {
                    filteredList.add(ticket)
                }
            }
        } else {
            filteredList.addAll(ticketList) // Add all tickets if no filter
        }
        // Notify the adapter that the data has changed
        recyclerViewStallOwnerOrdersAdapter.notifyDataSetChanged()
    }
}
