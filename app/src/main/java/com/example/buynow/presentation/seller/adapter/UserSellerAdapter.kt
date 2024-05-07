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
import com.example.buynow.data.model.User
import com.example.buynow.presentation.user.activity.ProductDetailsActivity
import java.text.DecimalFormat

class UserSellerAdapter(private val productList: ArrayList<User>, context: Context) :
    RecyclerView.Adapter<UserSellerAdapter.ViewHolder>(), Filterable {

    val ctx: Context = context
    var decimalFormat = DecimalFormat("#,###.##")
    private var filteredList: ArrayList<User> = productList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_admin_single, parent, false)
        return ViewHolder(productView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: User = filteredList[position]
        holder.userName.text = user.userName
        holder.userAddress.text = user.userAddress
        holder.userPhone.text = user.userPhone.toString()
        holder.countProduct.text = user.userCountProduct.toString()

        Glide.with(ctx)
            .load(user.userImage)
            .placeholder(bn)
            .into(holder.imgUser)

        holder.icEdit.setOnClickListener {
            goDetailsPage(user.userUid.toInt())
        }
        holder.icDelete.setOnClickListener {
            goDetailsPage(user.userUid.toInt())
        }

    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgUser: ImageView = itemView.findViewById(R.id.imgUser)
        val userName: TextView = itemView.findViewById(R.id.userName)
        val userPhone: TextView = itemView.findViewById(R.id.userPhone)
        val userAddress: TextView = itemView.findViewById(R.id.userAddress)
        val countProduct: TextView = itemView.findViewById(R.id.countProduct)
        val icEdit: ImageView = itemView.findViewById(R.id.icEdit)
        val icDelete: ImageView = itemView.findViewById(R.id.icDelete)
    }

    private fun goDetailsPage(position: Int) {
        val intent = Intent(ctx, ProductDetailsActivity::class.java)
        intent.putExtra("ProductID", position)
        ctx.startActivity(intent)
    }

    fun updateList(it: List<User>) {
        filteredList.clear()
        filteredList.addAll(it)
        notifyDataSetChanged()
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = mutableListOf<User>()

                if (constraint.isNullOrEmpty()) {
                    filteredResults.addAll(productList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (item in productList) {
                        if (item.userName.toLowerCase().contains(filterPattern)) {
                            filteredResults.add(item)
                        } else if (item.userName.toLowerCase().contains(filterPattern)) {
                            filteredResults.add(item)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredResults
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as? ArrayList<User> ?: arrayListOf()
                notifyDataSetChanged()
            }
        }
    }
}