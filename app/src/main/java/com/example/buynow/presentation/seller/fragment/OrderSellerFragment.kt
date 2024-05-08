package com.example.buynow.presentation.seller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.buynow.R
import com.example.buynow.data.local.room.AppDatabase
import com.example.buynow.data.local.room.SaleCartViewModel
import com.example.buynow.data.local.room.SaleCartViewModelFactory
import com.example.buynow.data.local.room.cart.CartViewModel
import com.example.buynow.data.local.room.sale.SaleViewModel
import com.example.buynow.presentation.seller.adapter.OrderSellerAdapter
import com.example.buynow.utils.FirebaseUtils
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrderSellerFragment : Fragment() {

    private lateinit var saleCartViewModel: SaleCartViewModel
    private lateinit var animationView: LottieAnimationView
    private lateinit var saleViewModel: SaleViewModel
    private lateinit var cartViewModel: CartViewModel
    lateinit var orderSellerAdapter: OrderSellerAdapter
    val userCollectionRef = Firebase.firestore.collection("Users")
    lateinit var adminUserRecView: RecyclerView
    lateinit var saleLayout: LinearLayout
    lateinit var etSearch: EditText
    lateinit var ibFilter: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_seller, container, false)
        adminUserRecView = view.findViewById(R.id.adminUserRecView)
        etSearch = view.findViewById(R.id.etSearch)
        ibFilter = view.findViewById(R.id.ibFilter)
        saleLayout = view.findViewById(R.id.saleLayout)
        saleViewModel = ViewModelProviders.of(this).get(SaleViewModel::class.java)
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        orderSellerAdapter = OrderSellerAdapter(requireContext())

        getUserData()

        adminUserRecView.layoutManager = LinearLayoutManager(requireContext())
        adminUserRecView.setHasFixedSize(true)
        adminUserRecView.adapter = orderSellerAdapter

        ibFilter.setOnClickListener {
            if (orderSellerAdapter != null) {
                orderSellerAdapter.filter.filter(etSearch.text.toString())
            }
        }
        return view
    }


    private fun getUserData() {
        try {

            val productDao = AppDatabase.getInstance(requireContext()).productDao()
            val saleDao = AppDatabase.getInstance(requireContext()).saleDao()
            val viewModelFactory = SaleCartViewModelFactory(productDao, saleDao)

            // Initialize ViewModel
            saleCartViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(SaleCartViewModel::class.java)

            // Call the function to get combined data
            saleCartViewModel.getCombinedData(FirebaseUtils.firebaseAuth.uid.toString())
                .observe(viewLifecycleOwner) { (sale, saleDetail) ->
                    orderSellerAdapter.updateList(saleDetail, sale)

                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}