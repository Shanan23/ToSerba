package com.example.buynow.presentation.user.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buynow.R
import com.example.buynow.R.drawable.bn
import com.example.buynow.data.local.room.item.ItemEntity
import com.example.buynow.presentation.user.activity.ProductDetailsActivity
import java.text.DecimalFormat

class ProductAdapter(private val productList: ArrayList<ItemEntity>, context: Context) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>(), Filterable {

    val ctx: Context = context
    var decimalFormat = DecimalFormat("#,###.##")
    private var filteredList: ArrayList<ItemEntity> = productList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView =
            LayoutInflater.from(parent.context).inflate(R.layout.single_product, parent, false)
        return ViewHolder(productView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: ItemEntity = filteredList[position]
        holder.productBrandName_singleProduct.text = product.category
        holder.productName_singleProduct.text = product.name
        holder.productPrice_singleProduct.text =
            "Rp " + decimalFormat.format(product.price)
        holder.productRating_singleProduct.rating = product.rating.toFloat()
        holder.lblRating.text = String.format("%.1f", (product.rating))

        Glide.with(ctx)
            .load(product.image)
            .placeholder(bn)
            .into(holder.productImage_singleProduct)

        if (product.discount == "") {
            holder.discount_singleProduct.visibility = View.VISIBLE
            holder.discountTv_singleProduct.text = "New"
        } else {
            holder.discountTv_singleProduct.text = product.discount
            holder.discount_singleProduct.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            goDetailsPage(product.pId.toInt())
        }

    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage_singleProduct: ImageView =
            itemView.findViewById(R.id.productImage_singleProduct)
        val productAddToFav_singleProduct: ImageView =
            itemView.findViewById(R.id.productAddToFav_singleProduct)
        val productRating_singleProduct: RatingBar =
            itemView.findViewById(R.id.productRating_singleProduct)
        val lblRating: TextView = itemView.findViewById(R.id.lblRating)
        val productBrandName_singleProduct: TextView =
            itemView.findViewById(R.id.productBrandName_singleProduct)
        val discountTv_singleProduct: TextView =
            itemView.findViewById(R.id.discountTv_singleProduct)
        val productName_singleProduct: TextView =
            itemView.findViewById(R.id.productName_singleProduct)
        val productPrice_singleProduct: TextView =
            itemView.findViewById(R.id.productPrice_singleProduct)
        val discount_singleProduct =
            itemView.findViewById<LinearLayout>(R.id.discount_singleProduct)
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