package com.rexdev.tasty_trends.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.rexdev.tasty_trends.adapter.RecyclerViewOrdersMenuAdapter
import com.rexdev.tasty_trends.dataClass.RefinedTicket
import com.rexdev.tasty_trends.domain.RetrofitInstance
import com.rexdev.tasty_trends.global.GlobalVariables
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewOrdersMenuAdapter: RecyclerViewOrdersMenuAdapter
    private var filter: String? = null
    private var ticketList: MutableList<RefinedTicket> = mutableListOf()
    private var filteredList: MutableList<RefinedTicket> = mutableListOf()
    private val app = GlobalVariables
    private lateinit var btn_back: ImageView
    private lateinit var btn_All: TextView
    private lateinit var btn_ToShip: TextView
    private lateinit var btn_ToRecieve: TextView
    private lateinit var btn_Completed: TextView
    private lateinit var btn_Cancelled: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order)

        btn_back = findViewById(R.id.btn_OrderBack)
        btn_All = findViewById(R.id.btn_all)
        btn_ToShip = findViewById(R.id.btn_toship)
        btn_ToRecieve = findViewById(R.id.btn_torecieved)
        btn_Completed = findViewById(R.id.btn_completed)
        btn_Cancelled = findViewById(R.id.btn_cancelled)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        loadUserTickets()

        btn_back.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        btn_All.setOnClickListener {
            filter = null
            updateButtonColors(btn_All)
            loadFilteredTickets()
        }
        btn_ToShip.setOnClickListener {
            filter = "Pending"
            updateButtonColors(btn_ToShip)
            loadFilteredTickets()
        }
        btn_ToRecieve.setOnClickListener {
            filter = "Accepted"
            updateButtonColors(btn_ToRecieve)
            loadFilteredTickets()
        }
        btn_Completed.setOnClickListener {
            filter = "Completed"
            updateButtonColors(btn_Completed)
            loadFilteredTickets()
        }
        btn_Cancelled.setOnClickListener {
            filter = "Cancelled"
            updateButtonColors(btn_Cancelled)
            loadFilteredTickets()
        }
    }

    private fun updateButtonColors(selectedButton: TextView) {
        btn_All.setTextColor(Color.BLACK)
        btn_ToShip.setTextColor(Color.BLACK)
        btn_ToRecieve.setTextColor(Color.BLACK)
        btn_Completed.setTextColor(Color.BLACK)
        btn_Cancelled.setTextColor(Color.BLACK)
        selectedButton.setTextColor(Color.GREEN)
    }

    private fun loadUserTickets() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getUserTickets(app.PROFILE_ID!!)
                if (response.success) {
                    ticketList.clear()
                    response.tickets?.let { tickets ->
                        ticketList.addAll(tickets)
                    }
                    Log.d("OrderActivity", "Tickets loaded: $ticketList")
                    withContext(Dispatchers.Main) {
                        loadFilteredTickets() // Update the filtered tickets after loading
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@OrderActivity, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@OrderActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadFilteredTickets() {
        filteredList.clear() // Clear the current filtered list

        if (filter != null) {
            filteredList.addAll(ticketList.filter { ticket ->
                ticket.status == filter
            })
        } else {
            filteredList.addAll(ticketList)
        }

        Log.d("OrderActivity", "Filtered List: $filteredList")
        recyclerViewOrdersMenuAdapter.updateTickets(filteredList) // Update the adapter with the filtered list
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rvUserTickets)
        recyclerViewOrdersMenuAdapter = RecyclerViewOrdersMenuAdapter(filteredList)
        val layoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerViewOrdersMenuAdapter
    }
}
