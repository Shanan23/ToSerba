package com.example.buynow.presentation.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.buynow.presentation.adapter.CoverProductAdapter
import com.example.buynow.presentation.adapter.ProductAdapter
import com.example.buynow.presentation.adapter.SaleProductAdapter

import com.example.buynow.data.model.Product

import com.example.buynow.R
import com.example.buynow.presentation.activity.VisualSearchActivity
import com.example.buynow.utils.StringUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.tasks.await
import java.io.IOException


class HomeFragment : Fragment() {

//    NewProducts.json

//    lateinit var coverRecView:RecyclerView
    lateinit var newRecView:RecyclerView
    lateinit var saleRecView:RecyclerView
    lateinit var coverProduct:ArrayList<Product>
    lateinit var newProduct:ArrayList<Product>
    lateinit var saleProduct:ArrayList<Product>

    lateinit var coverProductAdapter: CoverProductAdapter
    lateinit var newProductAdapter: ProductAdapter
    lateinit var saleProductAdapter: SaleProductAdapter

    lateinit var animationView: LottieAnimationView
    private val itemCollectionRef = Firebase.firestore.collection("Items")
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var newLayout:LinearLayout
    lateinit var saleLayout:LinearLayout



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        coverProduct = arrayListOf()
        newProduct = arrayListOf()
        saleProduct = arrayListOf()


//        coverRecView = view.findViewById(R.id.coverRecView)
        newRecView = view.findViewById(R.id.newRecView)
        saleRecView = view.findViewById(R.id.saleRecView)
        newLayout = view.findViewById(R.id.newLayout)
        saleLayout = view.findViewById(R.id.saleLayout)
        animationView = view.findViewById(R.id.animationView)



        val visualSearchBtn_homePage:ImageView = view.findViewById(R.id.visualSearchBtn_homePage)

        hideLayout()

//        getItems()

        newRecView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        newRecView.setHasFixedSize(true)
        newProductAdapter = ProductAdapter(newProduct, activity as Context )
        newRecView.adapter = newProductAdapter


        saleRecView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        saleRecView.setHasFixedSize(true)
        saleProductAdapter = SaleProductAdapter(saleProduct, activity as Context )
        saleRecView.adapter = saleProductAdapter

        visualSearchBtn_homePage.setOnClickListener {
            startActivity(Intent(context, VisualSearchActivity::class.java))
        }

        showLayout()

        return view
    }

    private suspend fun getItems() {
        val querySnapshot = itemCollectionRef
            .get().await()
    }


    private fun hideLayout(){
        animationView.playAnimation()
        animationView.loop(true)
        newLayout.visibility = View.GONE
        saleLayout.visibility = View.GONE
        animationView.visibility = View.VISIBLE
    }
    private fun showLayout(){
        animationView.pauseAnimation()
        animationView.visibility = View.GONE
        newLayout.visibility = View.VISIBLE
        saleLayout.visibility = View.VISIBLE
    }

//    private fun setCoverData() {
//
//        val jsonFileString = context?.let {
//
//            StringUtils.getJsonData(it, "CoverProducts.json")
//        }
//        val gson = Gson()
//
//        val listCoverType = object : TypeToken<List<Product>>() {}.type
//
//        var coverD: List<Product> = gson.fromJson(jsonFileString, listCoverType)
//
//        coverD.forEachIndexed { idx, person ->
//
//            coverProduct.add(person)
//            saleProduct.add(person)
//
//        }
//    }
//
//    private fun setNewProductData() {
//
//        val jsonFileString = context?.let {
//
//            StringUtils.getJsonData(it, "NewProducts.json")
//        }
//        val gson = Gson()
//
//        val listCoverType = object : TypeToken<List<Product>>() {}.type
//
//        var coverD: List<Product> = gson.fromJson(jsonFileString, listCoverType)
//
//        coverD.forEachIndexed { idx, person ->
//
//
//            newProduct.add(person)
//
//
//        }
//
//
//    }

}


