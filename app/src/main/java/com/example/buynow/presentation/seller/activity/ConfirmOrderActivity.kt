package com.example.buynow.presentation.seller.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProviders
import com.example.buynow.R
import com.example.buynow.data.local.room.cart.CartViewModel
import com.example.buynow.data.local.room.sale.SaleViewModel
import com.example.buynow.data.model.SaleCart
import com.example.buynow.utils.StringUtils.Companion.orderStatusList
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ConfirmOrderActivity : AppCompatActivity() {

    private lateinit var saleViewModel: SaleViewModel
    private lateinit var cartViewModel: CartViewModel
    private var selectedItem: String = ""
    private var saleCart: SaleCart? = null
    private var saleCartJson: String? = null
    private val llTitle: LinearLayout by lazy { findViewById<LinearLayout>(R.id.llTitle) }
    private val cvStatus: CardView by lazy { findViewById<CardView>(R.id.cvStatus) }
    private val orderStatus: TextView by lazy { findViewById<TextView>(R.id.orderStatus) }
    private val orderStatusValue: TextView by lazy { findViewById<TextView>(R.id.orderStatusValue) }
    private val orderDate: TextView by lazy { findViewById<TextView>(R.id.orderDate) }
    private val orderDateValue: TextView by lazy { findViewById<TextView>(R.id.orderDateValue) }
    private val cvUser: CardView by lazy { findViewById<CardView>(R.id.cvUser) }
    private val orderName: TextView by lazy { findViewById<TextView>(R.id.orderName) }
    private val orderNameValue: TextView by lazy { findViewById<TextView>(R.id.orderNameValue) }
    private val orderPhone: TextView by lazy { findViewById<TextView>(R.id.orderPhone) }
    private val orderPhoneValue: TextView by lazy { findViewById<TextView>(R.id.orderPhoneValue) }
    private val orderAddress: TextView by lazy { findViewById<TextView>(R.id.orderAddress) }
    private val orderAddressValue: TextView by lazy { findViewById<TextView>(R.id.orderAddressValue) }
    private val tv1: TextView by lazy { findViewById<TextView>(R.id.tv1) }
    private val tv2: TextView by lazy { findViewById<TextView>(R.id.tv2) }
    private val etResi: EditText by lazy { findViewById<EditText>(R.id.etResi) }
    private val tv3: TextView by lazy { findViewById<TextView>(R.id.tv3) }
    private val spinner: Spinner by lazy { findViewById<Spinner>(R.id.spinner) }
    private val btnSave: Button by lazy { findViewById<Button>(R.id.btnSave) }
    private val userCollectionRef = Firebase.firestore.collection("Users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)

        saleCartJson = intent.getStringExtra("saleCart")
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        saleViewModel = ViewModelProviders.of(this).get(SaleViewModel::class.java)

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, orderStatusList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItem = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        if (saleCartJson != null) {
            saleCart = Gson().fromJson(saleCartJson, SaleCart::class.java)

            orderStatusValue.text = saleCart?.saleEntity?.status
            orderDateValue.text = saleCart?.saleEntity?.date
            etResi.setText(saleCart?.productEntity?.resi!!)
            selectedItem = saleCart?.saleEntity?.status!!
            getUserData(saleCart?.saleEntity?.sUId!!)

            btnSave.setOnClickListener {
                var resi = etResi.text.toString()
                if (resi.isNullOrEmpty()) {
                    etResi.error = "Harap isi nomor resi"
                } else {
                    saleCart?.productEntity?.resi = resi
                    saleCart?.saleEntity?.status = selectedItem
                    saleViewModel.insertSale(saleCart?.saleEntity!!)
                    cartViewModel.updateCart(saleCart?.productEntity!!)
                }

                finish()
            }
        }
    }

    private fun getUserData(userId: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = userCollectionRef
                .document(userId)
                .get().await()

            val userPhone: String = querySnapshot.data?.get("userPhone").toString()
            val userName: String = querySnapshot.data?.get("userName").toString()
            val userAddress: String = querySnapshot.data?.get("userAddress").toString()
            val userShipmentAddress: String =
                querySnapshot.data?.get("userShipmentAddress").toString()
            withContext(Dispatchers.Main) {
                orderNameValue.text = userName
                orderPhoneValue.text = userPhone

                if (userShipmentAddress.equals(""))
                    orderAddressValue.text = userAddress
                else
                    orderAddressValue.text = userShipmentAddress
            }
        } catch (e: Exception) {

        }
    }
}