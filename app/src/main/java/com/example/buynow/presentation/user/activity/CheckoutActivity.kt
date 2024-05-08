package com.example.buynow.presentation.user.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.buynow.R
import com.example.buynow.data.local.room.cart.CartViewModel
import com.example.buynow.data.local.room.cart.ProductEntity
import com.example.buynow.data.local.room.sale.SaleEntity
import com.example.buynow.data.local.room.sale.SaleViewModel
import com.example.buynow.data.model.Courier
import com.example.buynow.presentation.user.adapter.CourierAdapter
import com.example.buynow.utils.Extensions.toast
import com.example.buynow.utils.FirebaseUtils.firebaseAuth
import com.example.buynow.utils.StringUtils.Companion.courierList
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date

class CheckoutActivity : AppCompatActivity(), CourierAdapter.OnCourierClickListener {
    private var sale: SaleEntity? = null
    private var item: ArrayList<ProductEntity>? = null
    private val llTitle: LinearLayout by lazy { findViewById<LinearLayout>(R.id.llTitle) }
    private val cvAddress: CardView by lazy { findViewById<CardView>(R.id.cvAddress) }
    private val tv1: TextView by lazy { findViewById<TextView>(R.id.tv1) }
    private val addAddress: TextView by lazy { findViewById<TextView>(R.id.addAddress) }
    private val cvAddressDetail: CardView by lazy { findViewById<CardView>(R.id.cvAddressDetail) }
    private val addressUsername: TextView by lazy { findViewById<TextView>(R.id.addressUsername) }
    private val addressEmail: TextView by lazy { findViewById<TextView>(R.id.addressEmail) }
    private val addressPhone: TextView by lazy { findViewById<TextView>(R.id.addressPhone) }
    private val addressLocation: TextView by lazy { findViewById<TextView>(R.id.addressLocation) }
    private val cbAddress: CheckBox by lazy { findViewById<CheckBox>(R.id.cbAddress) }
    private val cvPayment: CardView by lazy { findViewById<CardView>(R.id.cvPayment) }
    private val tv2: TextView by lazy { findViewById<TextView>(R.id.tv2) }
    private val changePayment: TextView by lazy { findViewById<TextView>(R.id.changePayment) }
    private val paymentLogo: ImageView by lazy { findViewById<ImageView>(R.id.paymentLogo) }
    private val paymentName: TextView by lazy { findViewById<TextView>(R.id.paymentName) }
    private val cvShipment: CardView by lazy { findViewById<CardView>(R.id.cvShipment) }
    private val tv3: TextView by lazy { findViewById<TextView>(R.id.tv3) }
    private val rvShipment: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rvShipment) }
    private val paymentSubTotal: TextView by lazy { findViewById<TextView>(R.id.paymentSubTotal) }
    private val paymentSubTotalValue: TextView by lazy { findViewById<TextView>(R.id.paymentSubTotalValue) }
    private val paymentDiscount: TextView by lazy { findViewById<TextView>(R.id.paymentDiscount) }
    private val paymentDiscountValue: TextView by lazy { findViewById<TextView>(R.id.paymentDiscountValue) }
    private val paymentShipment: TextView by lazy { findViewById<TextView>(R.id.paymentShipment) }
    private val paymentShipmentValue: TextView by lazy { findViewById<TextView>(R.id.paymentShipmentValue) }
    private val v1: View by lazy { findViewById<View>(R.id.v1) }
    private val paymentTotal: TextView by lazy { findViewById<TextView>(R.id.paymentTotal) }
    private val paymentTotalValue: TextView by lazy { findViewById<TextView>(R.id.paymentTotalValue) }
    private val paymentPolicy: TextView by lazy { findViewById<TextView>(R.id.paymentPolicy) }
    private val cbPolicy: CheckBox by lazy { findViewById<CheckBox>(R.id.cbPolicy) }
    private val v2: View by lazy { findViewById<View>(R.id.v2) }
    private val paymentGrandTotal: TextView by lazy { findViewById<TextView>(R.id.paymentGrandTotal) }
    private val paymentGrandTotalValue: TextView by lazy { findViewById<TextView>(R.id.paymentGrandTotalValue) }
    private val checkOutBagPage: Button by lazy { findViewById<Button>(R.id.checkOut_BagPage) }
    private lateinit var saleViewModel: SaleViewModel
    private lateinit var cartViewModel: CartViewModel
    lateinit var courierAdapter: CourierAdapter
    private val userCollectionRef = Firebase.firestore.collection("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        var saleJson = intent.getStringExtra("sale")
        var itemJson = intent.getStringExtra("item")

        saleViewModel = ViewModelProviders.of(this).get(SaleViewModel::class.java)
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        courierAdapter = CourierAdapter(courierList, this)

        courierAdapter.setOnCourierClickListener(this)
        rvShipment.adapter = courierAdapter
        if (saleJson != null) {
            sale = Gson().fromJson(saleJson, SaleEntity::class.java)
        }
        if (itemJson != null) {
            val listType: Type = object : TypeToken<ArrayList<ProductEntity>?>() {}.getType()
            item = Gson().fromJson(itemJson, listType)
        }

        addAddress.setOnClickListener {
            val intent = Intent(this@CheckoutActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        checkOutBagPage.setOnClickListener {
            if (cbPolicy.isActivated) {
                val epochSeconds = System.currentTimeMillis() / 1000
                val currentDate = Date()
                val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val formattedDateTime = formatter.format(currentDate)

                sale?.let { it1 -> saleViewModel.insertSale(it1) }
                item?.forEach {
                    if (it.isCheck) {
                        it.isPay = true
                        it.saleId = epochSeconds.toString()
                        cartViewModel.updateCart(it)
                    }
                }
            } else {
                toast("Silakan cek syarat dan ketentuan")
            }


            val intent = Intent(this@CheckoutActivity, HomeActivity::class.java)
            startActivity(intent)
        }


        if (sale != null) {
            (sale?.total!! + sale?.discount!!).toString().also { paymentSubTotalValue.text = it }
            paymentDiscountValue.text = sale?.discount!!.toString()
            paymentShipmentValue.text = sale?.sendAmount!!.toString()
            paymentTotalValue.text = sale?.total!!.toString()
            paymentGrandTotalValue.text = sale?.total!!.toString()
            getUserData()

        }
    }

    override fun onCourierClicked(courier: Courier) {
        sale?.courier = courier.name
        sale?.sendAmount = courier.price.toDouble()
        paymentShipmentValue.text = sale?.sendAmount!!.toString()
        sale?.total = sale?.total!! + courier.price.toDouble()

        paymentTotalValue.text = sale?.total!!.toString()
        paymentGrandTotalValue.text = sale?.total!!.toString()
    }

    private fun getUserData() = CoroutineScope(Dispatchers.IO).launch {
        try {

            val querySnapshot = userCollectionRef
                .document(firebaseAuth.uid.toString())
                .get().await()

            val userName: String = querySnapshot.data?.get("userName").toString()
            val userEmail: String = querySnapshot.data?.get("userEmail").toString()
            val userPhone: String = querySnapshot.data?.get("userPhone").toString()
            val userAddress: String = querySnapshot.data?.get("userAddress").toString()
            val userShipmentAddress: String =
                querySnapshot.data?.get("userShipmentAddress").toString()

            withContext(Dispatchers.Main) {
                addressUsername.text = userName
                addressPhone.text = userPhone
                addressEmail.text = userEmail
                if (userShipmentAddress.equals(""))
                    addressLocation.text = userAddress
                else
                    addressLocation.text = userShipmentAddress
            }
        } catch (e: Exception) {

        }
    }
}