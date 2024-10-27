package com.rexdev.tasty_trends.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.dataClass.CartItem
import com.rexdev.tasty_trends.dataClass.RefinedTicket
import com.rexdev.tasty_trends.domain.RetrofitInstance
import com.rexdev.tasty_trends.global.GlobalVariables
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerViewOrdersMenuAdapter(
    private var ticketList: MutableList<RefinedTicket>
) : RecyclerView.Adapter<RecyclerViewOrdersMenuAdapter.MyViewHolder>() {

    private val app = GlobalVariables

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_toship_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ticket = ticketList[position]
        holder.tvItemName.text = ticket.item_name
        holder.tvShopName.text = ticket.shop_name
        holder.tvTicketQuantity.text = ticket.quantity.toString()
        holder.tvTicketPrice.text = ticket.price.toString()
        holder.tvTicketStatus.text = ticket.status
        Picasso.get()
            .load(ticket.item_image)
            .placeholder(R.drawable.no_img_placeholder)
            .into(holder.ivTicketImg)

        holder.btn_Cancel.text = when (ticket.status) {
            "Pending" -> "Cancel"
            "Accepted" -> "Cannot be cancelled"
            "Cancelled" -> "Cancel"
            "Completed" -> "Close"
            else -> ""
        }

        holder.btn_Cancel.setOnClickListener {
            when (holder.btn_Cancel.text) {
                "Cancel" -> backToCart(ticket, holder.cardView)
                "Close" -> deleteTicket(ticket, holder.cardView)
            }
        }
    }

    fun updateTickets(newList: MutableList<RefinedTicket>) {
        ticketList = newList
        notifyDataSetChanged()
    }

    private fun deleteTicket(ticket: RefinedTicket, view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.deleteTicket(ticket.ticket_id)
                if (response.success) {
                    val position = ticketList.indexOf(ticket)
                    if (position != -1) {
                        ticketList.removeAt(position)
                        withContext(Dispatchers.Main) {
                            Snackbar.make(view, "Order Closed", Snackbar.LENGTH_SHORT).show()
                            notifyItemRemoved(position)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Failed to close order: ${response.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("DeleteTicketError", "Error during ticket deletion: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Snackbar.make(view, "Error: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun backToCart(ticket: RefinedTicket, view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.deleteTicket(ticket.ticket_id)
                if (response.success) {
                    app.CARTLIST.add(
                        CartItem(
                            shop_id = ticket.shop_id,
                            item_id = ticket.item_id,
                            item_name = ticket.item_name,
                            item_image = ticket.item_image,
                            quantity = ticket.quantity,
                            pricePerItem = (ticket.price / ticket.quantity)
                        )
                    )
                    val position = ticketList.indexOf(ticket)
                    if (position != -1) {
                        ticketList.removeAt(position)
                        withContext(Dispatchers.Main) {
                            Snackbar.make(view, "Order Cancelled", Snackbar.LENGTH_SHORT).show()
                            notifyItemRemoved(position)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Failed to cancel order: ${response.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("BackToCartError", "Error during back to cart: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Snackbar.make(view, "Error: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvShopName: TextView = itemView.findViewById(R.id.tvStallName)
        val ivTicketImg: ImageView = itemView.findViewById(R.id.ivTicketImg)
        val tvTicketQuantity: TextView = itemView.findViewById(R.id.tvTicketQuantity)
        val tvTicketPrice: TextView = itemView.findViewById(R.id.tvTicketPrice)
        val tvTicketStatus: TextView = itemView.findViewById(R.id.tvTicketStatus)
        val btn_Cancel: TextView = itemView.findViewById(R.id.btn_CancelOrder)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }
}
