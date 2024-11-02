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
import com.rexdev.tasty_trends.dataClass.RefinedTicket
import com.rexdev.tasty_trends.dataClass.UpdateTicketStatus
import com.rexdev.tasty_trends.domain.RetrofitInstance
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerViewStallOwnerOrdersAdapter(
    private var ticketList: MutableList<RefinedTicket>
) : RecyclerView.Adapter<RecyclerViewStallOwnerOrdersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_customerorder_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ticket = ticketList[position]
        if (ticket.status != "Completed") {
            holder.tvBuyerName.text = ticket.buyer_name
            holder.tvItemPrice.text = ticket.price.toString()
            holder.tvQuantity.text = ticket.quantity.toString()
            holder.tvTicketStatus.text = ticket.status
            holder.tvLocation.text = ticket.location

            // Check if item image is not null or empty
            if (!ticket.item_image.isNullOrEmpty()) {
                Picasso.get()
                    .load(ticket.item_image)
                    .placeholder(R.drawable.no_img_placeholder)
                    .into(holder.ivTicketImg)
            } else {
                holder.ivTicketImg.setImageResource(R.drawable.no_img_placeholder) // Default image if URL is invalid
            }

            when (ticket.status) {
                "Cancelled" -> {
                    holder.btn_DeleteTicket.visibility = View.GONE
                    holder.btn_AcceptTicket.visibility = View.GONE
                }
                "Accepted" -> {
                    holder.tvTicketStatus.text = "Accepted"
                    holder.btn_DeleteTicket.visibility = View.GONE
                    holder.btn_AcceptTicket.text = "Complete"
                    ticket.status = "Accepted"
                }
            }

            holder.btn_DeleteTicket.setOnClickListener {
                DeclineTicket(ticket, holder.cardView)
                holder.tvTicketStatus.text = "Cancelled"
                ticket.status = "Cancelled"
                holder.btn_DeleteTicket.visibility = View.GONE
                holder.btn_AcceptTicket.visibility = View.GONE
            }
            holder.btn_AcceptTicket.setOnClickListener {
                when(ticket.status) {
                    "Pending" -> {
                        AcceptTicket(ticket, holder.cardView)
                        holder.tvTicketStatus.text = "Accepted"
                        holder.btn_DeleteTicket.visibility = View.GONE
                        holder.btn_AcceptTicket.text = "Complete"
                        ticket.status = "Accepted"
                    }
                    "Accepted" -> CompleteTicket(ticket, holder.cardView)
                }
            }
        }
        else {
            ticketList.remove(ticket)
            notifyDataSetChanged()
        }
    }

    fun updateTicketList(newTicketList: List<RefinedTicket>) {
        ticketList.clear()
        ticketList.addAll(newTicketList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBuyerName: TextView = itemView.findViewById(R.id.tvBuyerName)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val ivTicketImg: ImageView = itemView.findViewById(R.id.ivTicketImg)
        val btn_AcceptTicket: TextView = itemView.findViewById(R.id.btn_AcceptTicket)
        val btn_DeleteTicket: TextView = itemView.findViewById(R.id.btn_deleteTicket)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val tvItemPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
        val tvTicketStatus: TextView = itemView.findViewById(R.id.tvTicketStatus)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    private fun AcceptTicket(ticket: RefinedTicket, view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.updateTicketStatus(ticket.ticket_id,
                    UpdateTicketStatus(status = "Accepted"))
                if (response.success == true) {
                    ticket.status = "Accepted"  // Update the ticket status
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Ticket accepted successfully", Snackbar.LENGTH_SHORT).show()
                        notifyItemChanged(ticketList.indexOf(ticket)) // Update UI for this ticket
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Failed to accept ticket: ${response.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("AcceptTicketError", "Error accepting ticket: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Snackbar.make(view, "Error: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun DeclineTicket(ticket: RefinedTicket, view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.updateTicketStatus(ticket.ticket_id,
                    UpdateTicketStatus(status = "Cancelled"))
                if (response.success == true) {
                    ticket.status = "Cancelled"  // Update the ticket status
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Ticket declined successfully", Snackbar.LENGTH_SHORT).show()
                        notifyItemChanged(ticketList.indexOf(ticket)) // Update UI for this ticket
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Failed to decline ticket: ${response.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("DeclineTicketError", "Error declining ticket: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Snackbar.make(view, "Error: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun CompleteTicket(ticket: RefinedTicket, view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.updateTicketStatus(ticket.ticket_id,
                    UpdateTicketStatus(status = "Completed"))
                if (response.success == true) {
                    val position = ticketList.indexOf(ticket)
                    ticketList.removeAt(position)  // Remove from the list
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Ticket completed successfully", Snackbar.LENGTH_SHORT).show()
                        notifyItemRemoved(position)  // Notify RecyclerView to remove the item
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(view, "Failed to complete ticket: ${response.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("CompleteTicketError", "Error completing ticket: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Snackbar.make(view, "Error: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
