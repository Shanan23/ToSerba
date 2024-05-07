package com.example.buynow.presentation.seller.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buynow.R
import com.example.buynow.R.drawable.ic_admin_3
import com.example.buynow.data.model.SummaryAdmin

class SummarySellerAdapter(private val dataList: List<SummaryAdmin>, context: Context) :
    RecyclerView.Adapter<SummarySellerAdapter.ViewHolder>() {

    val ctx: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.summary_admin_single, parent, false)
        return ViewHolder(productView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val summaryAdmin: SummaryAdmin = dataList[position]
        holder.summaryQty.text = summaryAdmin.amount.toString()
        holder.summaryDesc.text = summaryAdmin.name

        Glide.with(ctx)
            .load(summaryAdmin.image)
            .placeholder(ic_admin_3)
            .into(holder.imgSummary)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSummary: ImageView = itemView.findViewById(R.id.imgSummary)
        val summaryDesc: TextView = itemView.findViewById(R.id.summaryDesc)
        val summaryQty: TextView = itemView.findViewById(R.id.summaryQty)
    }
}