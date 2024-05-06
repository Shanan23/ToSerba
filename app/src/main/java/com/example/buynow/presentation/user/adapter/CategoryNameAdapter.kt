package com.example.buynow.presentation.user.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.buynow.R
import com.example.buynow.data.model.Category

class CategoryNameAdapter(val dataList: List<Category>) :
    RecyclerView.Adapter<CategoryNameAdapter.ViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION
    private var onCategoryClickListener: OnCategoryClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryNameAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_name_single, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CategoryNameAdapter.ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item.Name)
        holder.itemView.setOnClickListener {
            selectedItemPosition = position
            onCategoryClickListener?.onCategoryClicked(item)
            notifyDataSetChanged()
        }
        // Change background color of the selected item
        holder.itemView.setBackgroundColor(
            if (selectedItemPosition == position) {
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.backgroundLike
                )
            } else {
                Color.WHITE
            }
        )

        val textColor = if (selectedItemPosition == position) ContextCompat.getColor(
            holder.itemView.context,
            R.color.white
        ) else ContextCompat.getColor(holder.itemView.context, R.color.mainText)
        holder.categoryName.setTextColor(textColor)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val categoryName: TextView = itemView.findViewById(R.id.categoryName)

        fun bind(item: String) {
            categoryName.text = item
        }
    }
    fun setOnCategoryClickListener(listener: OnCategoryClickListener) {
        onCategoryClickListener = listener
    }
    interface OnCategoryClickListener {
        fun onCategoryClicked(category: Category)
    }
}