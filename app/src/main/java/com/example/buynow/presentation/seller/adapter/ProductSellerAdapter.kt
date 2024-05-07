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
import com.bumptech.glide.Glide
import com.example.buynow.R
import com.example.buynow.R.drawable.bn
import com.example.buynow.data.local.room.item.ItemEntity
import com.example.buynow.presentation.user.activity.ProductDetailsActivity
import java.text.DecimalFormat

class ProductSellerAdapter(private val productList: ArrayList<ItemEntity>, context: Context) :
    RecyclerView.Adapter<ProductSellerAdapter.ViewHolder>(), Filterable {

    val ctx: Context = context
    var decimalFormat = DecimalFormat("#,###.##")
    private var filteredList: ArrayList<ItemEntity> = productList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.product_admin_single, parent, false)
        return ViewHolder(productView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: ItemEntity = filteredList[position]
        holder.productCategory.text = product.category
        holder.productName.text = product.name
        holder.productStock.text = product.stock.toString()
        holder.productPrice.text =
            "Rp " + decimalFormat.format(product.price)

        Glide.with(ctx)
            .load(product.image)
            .placeholder(bn)
            .into(holder.imgProduct)

        holder.icEdit.setOnClickListener {
            goDetailsPage(product.pId.toInt())
        }
        holder.icDelete.setOnClickListener {
            goDetailsPage(product.pId.toInt())
        }

    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        val productName: TextView = itemView.findViewById(R.id.userName)
        val productCategory: TextView = itemView.findViewById(R.id.userPhone)
        val productPrice: TextView = itemView.findViewById(R.id.userAddress)
        val productStock: TextView = itemView.findViewById(R.id.productStock)
        val icEdit: ImageView = itemView.findViewById(R.id.icEdit)
        val icDelete: ImageView = itemView.findViewById(R.id.icDelete)
    }

    private fun goDetailsPage(position: Int) {
        val intent = Intent(ctx, ProductDetailsActivity::class.java)
        intent.putExtra("ProductID", position)
        ctx.startActivity(intent)
    }

    fun updateList(it: List<ItemEntity>) {
        filteredList.clear()
        filteredList.addAll(it)
        notifyDataSetChanged()
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = mutableListOf<ItemEntity>()

                if (constraint.isNullOrEmpty()) {
                    filteredResults.addAll(productList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (item in productList) {
                        if (item.category.toLowerCase().contains(filterPattern)) {
                            filteredResults.add(item)
                        } else if (item.name.toLowerCase().contains(filterPattern)) {
                            filteredResults.add(item)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredResults
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as? ArrayList<ItemEntity> ?: arrayListOf()
                notifyDataSetChanged()
            }
        }
    }
}