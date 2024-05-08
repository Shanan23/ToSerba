package com.example.buynow.presentation.seller.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buynow.R
import com.example.buynow.data.local.room.cart.ProductEntity
import com.example.buynow.data.local.room.sale.SaleEntity
import com.example.buynow.data.model.SaleCart
import com.example.buynow.presentation.seller.activity.ConfirmOrderActivity
import com.google.gson.Gson
import java.text.DecimalFormat

class OrderSellerAdapter(
    context: Context

) :
    RecyclerView.Adapter<OrderSellerAdapter.ViewHolder>(), Filterable {

    private lateinit var saleEntities: Collection<SaleCart>
    val ctx: Context = context
    var decimalFormat = DecimalFormat("#,###.##")
    private var filteredList: ArrayList<SaleCart> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.order_seller_single, parent, false)
        return ViewHolder(productView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val saleCartEntity = filteredList[position]
        holder.orderStatus.text = saleCartEntity.saleEntity.status
        holder.orderDate.text = saleCartEntity.saleEntity.date
        holder.orderTotal.text =
            decimalFormat.format(saleCartEntity.productEntity.subTotal).toString()
        holder.orderDetail.text =
            saleCartEntity.productEntity.name + " x " + saleCartEntity.productEntity.qua
        holder.icDetail.setOnClickListener {
            saleCartEntity.productEntity.id.let { it1 -> goDetailsPage(saleCartEntity) }
        }
        holder.icDelete.setOnClickListener {
            saleCartEntity.productEntity.id.let { it1 -> goDetailsPage(saleCartEntity) }
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderStatus: TextView = itemView.findViewById(R.id.orderStatus)
        val orderDate: TextView = itemView.findViewById(R.id.orderDate)
        val orderDetail: TextView = itemView.findViewById(R.id.orderDetail)
        val orderTotal: TextView = itemView.findViewById(R.id.orderTotal)
        val icDetail: ImageView = itemView.findViewById(R.id.icDetail)
        val icDelete: ImageView = itemView.findViewById(R.id.icDelete)
    }

    private fun goDetailsPage(saleCart: SaleCart) {
        val intent = Intent(ctx, ConfirmOrderActivity::class.java)
        intent.putExtra("saleCart", Gson().toJson(saleCart))
        ctx.startActivity(intent)
    }

    fun updateList(saleEntityList: List<SaleEntity>, productEntityList: List<ProductEntity>) {
        var saleCarts: ArrayList<SaleCart> = arrayListOf()
        productEntityList.forEach { productEntity ->
            var saleByCart = saleEntityList.find { sale -> sale.sId.equals(productEntity.saleId) }
            var saleCart = SaleCart(productEntity, saleByCart!!)
            saleCarts.add(saleCart)
        }
        filteredList.clear()
        filteredList.addAll(saleCarts)
        notifyDataSetChanged()
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = mutableListOf<SaleCart>()

                if (constraint.isNullOrEmpty()) {
                    filteredResults.addAll(saleEntities)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (item in saleEntities) {
                        if (item.saleEntity.status.toLowerCase().contains(filterPattern)) {
                            filteredResults.add(item)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredResults
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as? ArrayList<SaleCart> ?: arrayListOf()
                notifyDataSetChanged()
            }
        }
    }
}