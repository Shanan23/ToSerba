package com.example.buynow.presentation.admin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buynow.R
import com.example.buynow.data.local.room.AppDatabase
import com.example.buynow.data.local.room.SummaryViewModel
import com.example.buynow.data.local.room.SummaryViewModelFactory
import com.example.buynow.data.local.room.item.ItemEntity
import com.example.buynow.data.local.room.item.ItemViewModel
import com.example.buynow.presentation.admin.adapter.ProductAdminAdapter
import com.example.buynow.presentation.admin.adapter.SummaryAdminAdapter

class ProductAdminFragment : Fragment() {

    lateinit var adminProductRecView: RecyclerView
    lateinit var productAdminAdapter: ProductAdminAdapter
    lateinit var etSearch: EditText
    lateinit var ibFilter: ImageButton
    lateinit var saleProduct: ArrayList<ItemEntity>
    lateinit var itemViewModel: ItemViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_admin, container, false)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        adminProductRecView = view.findViewById(R.id.adminProductRecView)
        etSearch = view.findViewById(R.id.etSearch)
        ibFilter = view.findViewById(R.id.ibFilter)
        saleProduct = arrayListOf()
        getItems()

        adminProductRecView.layoutManager = LinearLayoutManager(requireContext())
        adminProductRecView.setHasFixedSize(true)
        productAdminAdapter = ProductAdminAdapter(saleProduct, requireContext())
        adminProductRecView.adapter = productAdminAdapter

        ibFilter.setOnClickListener {
            productAdminAdapter.filter.filter(etSearch.text.toString())
        }


        return view
    }

    private fun getItems() {
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)

        itemViewModel.allItems.observe(viewLifecycleOwner, Observer { List ->
            List?.let {
                var itemSize = it.size
                productAdminAdapter.updateList(it)

            }
        })
    }
}