package com.rexdev.tasty_trends.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rexdev.tasty_trends.dataClass.CartItem
import com.rexdev.tasty_trends.dataClass.ShopItem
import com.rexdev.tasty_trends.global.GlobalVariables
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.tools.ClickListenerInit
import com.squareup.picasso.Picasso


class RecyclerViewStallsMenuAdapter(
    private val shopItemList: MutableList<ShopItem>
) : RecyclerView.Adapter<RecyclerViewStallsMenuAdapter.MyViewHolder>() {
    private val listener = ClickListenerInit()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_stall_menu_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = shopItemList.get(position)
            holder.tvmenuName.text = item.itemName
            holder.tvprice.text = "₱${item.price}"
        // Load image using Glide or Picasso
            Picasso.get()
                .load(item.image)
                .placeholder(R.drawable.no_img_placeholder)
                .into(holder.ivStallmenuImg)

        holder.btn_favorite.setOnClickListener {
                listener.onFavoriteClick(item)
        }

        holder.tvaddcart.setOnClickListener {
            // Create a CartItem with quantity set to 1
            val cartItem = item.let { it1 ->
                CartItem(
                    shopId = it1.shopId,
                    itemId = it1.itemId,
                    itemName = item.itemName,
                    itemImage = item.image,
                    quantity = 1,
                    pricePerItem = item.price
                )
            }

            if(!GlobalVariables.CARTLIST.contains(cartItem)) {
                listener.onCartClick(cartItem) // Pass the CartItem to the listener
                Snackbar.make(
                    holder.itemView,
                    "${item.itemName} added to cart",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            else {
                Snackbar.make(
                    holder.itemView,
                    "${item.itemName} is already in cart",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return shopItemList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvmenuName: TextView = itemView.findViewById(R.id.tvmenuName)
        val ivStallmenuImg: ImageView = itemView.findViewById(R.id.ivStallmenuImg)
        val tvprice: TextView = itemView.findViewById(R.id.tvPrice)
        val btn_favorite: ImageView = itemView.findViewById(R.id.btn_favorite)
        val tvaddcart: TextView = itemView.findViewById(R.id.tvaddcart)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

}
