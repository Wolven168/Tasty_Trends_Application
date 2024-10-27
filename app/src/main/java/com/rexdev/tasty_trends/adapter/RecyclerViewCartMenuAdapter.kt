package com.rexdev.tasty_trends.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rexdev.tasty_trends.dataClass.CartItem
import com.rexdev.tasty_trends.global.GlobalVariables
import com.rexdev.tasty_trends.R
import com.squareup.picasso.Picasso


class RecyclerViewCartMenuAdapter(
) : RecyclerView.Adapter<RecyclerViewCartMenuAdapter.MyViewHolder>() {
    private val app = GlobalVariables
    private var cartItemList = app.CARTLIST

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_cart_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cartItem = cartItemList[position]
        holder.tvItemName.text = cartItem.item_name
        holder.tvQuantity.text = "${cartItem.quantity}"
        holder.tvPrice.text = "Price: ₱${cartItem.totalPrice()}"

        Picasso.get()
            .load(cartItem.item_image)
            .placeholder(R.drawable.no_img_placeholder)
            .into(holder.cartImage)

        holder.btnMinus.setOnClickListener {
            if (cartItem.quantity > 0) { // Prevent negative quantity
                cartItem.quantity -= 1
                notifyItemChanged(position) // Notify adapter to update this item
            }
        }

        holder.btnAdd.setOnClickListener {
            cartItem.quantity += 1
            notifyItemChanged(position) // Notify adapter to update this item
        }

        holder.btnRemove.setOnClickListener {
            removeItem(holder.position)
        }
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < cartItemList.size) {
            cartItemList.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    override fun getItemCount(): Int = cartItemList.size

    fun submitList(cartItems: MutableList<CartItem>) {
        cartItemList = cartItems
        notifyDataSetChanged() // Notify adapter of new data
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartImage: ImageView = itemView.findViewById(R.id.ivcartImg)
        val tvItemName: TextView = itemView.findViewById(R.id.tvCartItemName)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvCartQuantity)
        val btnMinus: TextView = itemView.findViewById(R.id.btn_Cartminus)
        val btnAdd: TextView = itemView.findViewById(R.id.btn_CartAdd)
        val tvPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)
        val btnRemove: TextView = itemView.findViewById(R.id.btnRemoveCart)
        val cardview: CardView = itemView.findViewById(R.id.cardView)
    }
}
