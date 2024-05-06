package com.example.buynow.presentation.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buynow.R
import com.example.buynow.data.local.room.cart.ProductEntity

class CartAdapter(private val ctx: Context, val listener: CartItemClickAdapter):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val cartList: ArrayList<ProductEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val cartView = LayoutInflater.from(ctx).inflate(R.layout.cart_item_single, parent, false)

        return CartViewHolder(cartView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {


        val cartItem: ProductEntity = cartList[position]

        holder.cartName.text = cartItem.name
        holder.cartPrice.text = "Rp " + cartItem.subTotal
        holder.quantityTvCart.text = cartItem.qua.toString()
        holder.checkCart.isChecked = cartItem.isCheck

        Glide.with(ctx)
            .load(cartItem.Image)
            .into(holder.cartImage)

        holder.checkCart.setOnCheckedChangeListener { compoundButton, b ->
            cartItem.isCheck = b
            listener.onItemUpdateClick(cartItem)
        }

        holder.cartMore.setOnClickListener {
            listener.onItemDeleteClick(cartItem)
        }

        holder.minusLayout.setOnClickListener {
            if (cartItem.qua != 1) {
                cartItem.qua--
                cartItem.subTotal = cartItem.price * cartItem.qua
                listener.onItemUpdateClick(cartItem)
            } else {
                listener.onItemDeleteClick(cartItem)
            }
        }

        holder.plusLayout.setOnClickListener {
            cartItem.qua++
            cartItem.subTotal = cartItem.price * cartItem.qua
            listener.onItemUpdateClick(cartItem)
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }


    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cartImage: ImageView = itemView.findViewById(R.id.cartImage)
        val cartMore: ImageView = itemView.findViewById(R.id.cartMore)
        val cartName: TextView = itemView.findViewById(R.id.cartName)
        val cartPrice: TextView = itemView.findViewById(R.id.cartPrice)
        val quantityTvCart: TextView = itemView.findViewById(R.id.quantityTvCart)
        val checkCart: CheckBox = itemView.findViewById(R.id.checkCart)
        val minusLayout: LinearLayout = itemView.findViewById(R.id.minusLayout)
        val plusLayout: LinearLayout = itemView.findViewById(R.id.plusLayout)


    }

    fun updateList(newList: List<ProductEntity>){
        cartList.clear()
        cartList.addAll(newList)
        notifyDataSetChanged()
    }


}

interface CartItemClickAdapter{
    fun onItemDeleteClick(product: ProductEntity)
    fun onItemUpdateClick(product: ProductEntity)


}