package com.example.buynow.presentation.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.buynow.R
import com.example.buynow.data.local.room.sale.SaleEntity

class SaleAdapter(private val ctx: Context, val listener: SaleItemClickAdapter) :
    RecyclerView.Adapter<SaleAdapter.SaleViewHolder>() {

    private val cartList: ArrayList<SaleEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val cartView = LayoutInflater.from(ctx).inflate(R.layout.recent_single, parent, false)

        return SaleViewHolder(cartView)
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {


        val cartItem: SaleEntity = cartList[position]

        holder.trxResi.text = "Lacak nomor resi : " + cartItem.resi
        holder.countDetail.text = "Jumlah : " + cartItem.detail
        holder.trxDate.text = cartItem.date
        holder.trxStatus.text = cartItem.status

        holder.cvUpload.setOnClickListener {
            listener.onItemUpdateClick(cartItem)
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }


    class SaleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val trxResi: TextView = itemView.findViewById(R.id.trxResi)
        val countDetail: TextView = itemView.findViewById(R.id.countDetail)
        val trxDate: TextView = itemView.findViewById(R.id.trxDate)
        val trxStatus: TextView = itemView.findViewById(R.id.trxStatus)
        val cvUpload: CardView = itemView.findViewById(R.id.cvUpload)


    }

    fun updateList(newList: List<SaleEntity>) {
        cartList.clear()
        cartList.addAll(newList)
        notifyDataSetChanged()
    }


}

interface SaleItemClickAdapter {
    fun onItemDeleteClick(saleEntity: SaleEntity)
    fun onItemUpdateClick(saleEntity: SaleEntity)


}