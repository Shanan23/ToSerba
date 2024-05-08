package com.example.buynow.presentation.user.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buynow.R
import com.example.buynow.data.model.Courier

class CourierAdapter(private val dataList: List<Courier>, context: Context) :
    RecyclerView.Adapter<CourierAdapter.ViewHolder>() {

    val ctx: Context = context
    private var onCourierClickListener: OnCourierClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.courier_single, parent, false)
        return ViewHolder(productView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val courier: Courier = dataList[position]
        holder.bind(ctx, courier)
        holder.itemView.setOnClickListener {

            onCourierClickListener?.onCourierClicked(courier)
            notifyDataSetChanged()
        }

//        Glide.with(ctx)
//            .load(summaryAdmin.image)
//            .placeholder(ic_admin_3)
//            .into(holder.imgSummary)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCourier: ImageView = itemView.findViewById(R.id.imgCourier)
        val courierEstimate: TextView = itemView.findViewById(R.id.courierEstimate)

        fun bind(ctx: Context, item: Courier) {
            courierEstimate.text = item.estimation
            val resourceID: Int = ctx.getResources().getIdentifier(
                item.logo,
                "drawable",
                ctx.getPackageName()
            )
            Glide.with(ctx)
                .load(resourceID)
                .placeholder(android.R.drawable.ic_input_add)
                .into(imgCourier)
        }
    }

    fun setOnCourierClickListener(listener: OnCourierClickListener) {
        onCourierClickListener = listener
    }

    interface OnCourierClickListener {
        fun onCourierClicked(courier: Courier)
    }
}