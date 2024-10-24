package com.rexdev.tasty_trends.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rexdev.tasty_trends.activity.HomeActivity
import com.rexdev.tasty_trends.dataClass.Stalls
import com.rexdev.tasty_trends.R
import com.squareup.picasso.Picasso

class RecyclerViewStallsAdapter(
    private val activity: HomeActivity,
    private val stallsList: List<Stalls>
) : RecyclerView.Adapter<RecyclerViewStallsAdapter.MyViewHolder>() {

    var onItemClick: ((Stalls) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_stall_lists_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val stall = stallsList[position]
        val stallId = stall.shopId
        val stallName = stall.shopName
        val stallImg = stall.image

        // Assuming stall.image is a URL
        Picasso.get()
            .load(stall.image)
            .placeholder(R.drawable.no_img_placeholder)
            .into(holder.ivStallsImg)

        holder.cardView.setOnClickListener {
            val stallData = Stalls(stallId, stallName, stallImg)
            onItemClick?.invoke(stallData)
        }
    }

    override fun getItemCount(): Int {
        return stallsList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStallsImg: ImageView = itemView.findViewById(R.id.ivStallImg)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }
}

