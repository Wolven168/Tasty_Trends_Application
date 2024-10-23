package com.rexdev.tasty_trends.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rexdev.tasty_trends.Global.GlobalVariables
import com.rexdev.tasty_trends.R
import com.squareup.picasso.Picasso


class RecyclerViewCartMenuAdapter(
) : RecyclerView.Adapter<RecyclerViewCartMenuAdapter.MyViewHolder>() {
    //val listener = ClickListenerInit()
    val app = GlobalVariables
    var cartItemList = app.CARTLIST

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_cart_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cartItem = cartItemList[position]
        holder.tvItemName.text = cartItem.itemId // Or use a method to get the name
        holder.tvQuantity.text = "Quantity: ${cartItem.quantity}"
        holder.tvPrice.text = "Price: ${cartItem.pricePerItem * cartItem.quantity}"

        Picasso.get()
            .load(cartItem.itemImage)
            .placeholder(R.drawable.no_img_placeholder)
            .into(holder.cartImage)

        holder.btnMinus.setOnClickListener { // Minus button
            cartItem.quantity -= 1
        }

        holder.btnAdd.setOnClickListener { // Add button
            cartItem.quantity += 1
        }

        // Remove item button
//        holder.btnRemove.setOnClickListener {
//            listener.onRemoveCart(cartItem)
//        }
    }

    override fun getItemCount(): Int = cartItemList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cartImage: ImageView = itemView.findViewById(R.id.ivcartImg)
        val tvItemName: TextView = itemView.findViewById(R.id.tvCartItemName)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvCartQuantity)
        val btnMinus: Button = itemView.findViewById(R.id.btn_Cartminus)
        val btnAdd: Button = itemView.findViewById(R.id.btn_CartAdd)
        val tvPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)
//        val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
        val cardview: CardView = itemView.findViewById(R.id.cardView)

    }

}
