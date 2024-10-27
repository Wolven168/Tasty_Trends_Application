package com.rexdev.tasty_trends.adapter

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
import com.rexdev.tasty_trends.dataClass.Ticket
import com.rexdev.tasty_trends.global.GlobalVariables
import com.rexdev.tasty_trends.tools.ClickListenerInit
import com.squareup.picasso.Picasso

//class RecyclerViewStallOwnerMenuAdapter(
//) : RecyclerView.Adapter<RecyclerViewStallOwnerMenuAdapter.MyViewHolder>() {
//    private val listener = ClickListenerInit()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_stall_menu_list, parent, false)
//        return MyViewHolder(view)
//    }

//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val item = ticketList.get(position)
//
//        holder.tvBuyerName.text = item.item_name
//        holder.tvprice.text = "₱${item.item_price}"
//        // Load image using Glide or Picasso
//        Picasso.get()
//            .load(item.item_image)
//            .placeholder(R.drawable.no_img_placeholder)
//            .into(holder.ivStallmenuImg)
//
//        holder.btn_favorite.setOnClickListener {
//            if(!GlobalVariables.FAVLIST.contains(item)) {
//                listener.onFavoriteClick(item) // Pass the CartItem to the listener
//                Snackbar.make(
//                    holder.itemView,
//                    "${item.item_name} added to favorites",
//                    Snackbar.LENGTH_SHORT
//                ).show()
//            }
//            else {
//                Snackbar.make(
//                    holder.itemView,
//                    "${item.item_name} is already in favorites",
//                    Snackbar.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//        holder.tvaddcart.setOnClickListener {
//            // Create a CartItem with quantity set to 1
//            val cartItem = item.let { it1 ->
//                CartItem(
//                    shop_id = it1.shop_id,
//                    item_id = it1.item_id,
//                    item_name = item.item_name,
//                    item_image = item.item_image,
//                    quantity = 1,
//                    pricePerItem = item.item_price
//                )
//            }
//
//            if(!GlobalVariables.CARTLIST.contains(cartItem)) {
//                listener.onCartClick(cartItem) // Pass the CartItem to the listener
//                Snackbar.make(
//                    holder.itemView,
//                    "${item.item_name} added to cart",
//                    Snackbar.LENGTH_SHORT
//                ).show()
//            }
//            else {
//                Snackbar.make(
//                    holder.itemView,
//                    "${item.item_name} is already in cart",
//                    Snackbar.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return shopItemList.size
//    }
//
//    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvBuyerName: TextView = itemView.findViewById(R.id.tvBuyerName)
//        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
//        val ivTicketImg: ImageView = itemView.findViewById(R.id.ivTicketImg)
//        val btn_AcceptTicket: TextView = itemView.findViewById(R.id.btn_AcceptTicket)
//        val btn_DeleteTicket: TextView = itemView.findViewById(R.id.btn_deleteTicket)
//        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
//        val tvItemPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
//        val cardView: CardView = itemView.findViewById(R.id.cardView)
//    }
//
//}