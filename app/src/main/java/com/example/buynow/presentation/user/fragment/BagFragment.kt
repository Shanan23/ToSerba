package com.example.buynow.presentation.user.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.buynow.R
import com.example.buynow.data.local.room.cart.CartViewModel
import com.example.buynow.data.local.room.cart.ProductEntity
import com.example.buynow.data.local.room.sale.SaleEntity
import com.example.buynow.data.local.room.sale.SaleViewModel
import com.example.buynow.presentation.user.activity.CheckoutActivity
import com.example.buynow.presentation.user.adapter.CartAdapter
import com.example.buynow.presentation.user.adapter.CartItemClickAdapter
import com.example.buynow.utils.Extensions.toast
import com.example.buynow.utils.StringUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date

class BagFragment : Fragment(), CartItemClickAdapter {

    lateinit var cartRecView: RecyclerView
    lateinit var cartAdapter: CartAdapter
    lateinit var animationView: LottieAnimationView
    lateinit var totalPriceBagFrag: TextView
    lateinit var discountTotalPriceBagFrag: TextView
    lateinit var subTotalPriceBagFrag: TextView
    lateinit var checkOut_BagPage: Button
    lateinit var etVoucher: EditText
    lateinit var applyVoucher: LinearLayout
    lateinit var item: ArrayList<ProductEntity>
    var sum: Int = 0
    var countDetail: Int = 0
    var subTotal: Int = 0
    var discount: Int = 0

    var total: Int = 0
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var cartViewModel: CartViewModel
    private lateinit var saleViewModel: SaleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_bag, container, false)

        cartRecView = view.findViewById(R.id.cartRecView)
        animationView = view.findViewById(R.id.animationViewCartPage)
        totalPriceBagFrag = view.findViewById(R.id.totalPriceBagFrag)
        discountTotalPriceBagFrag = view.findViewById(R.id.discountTotalPriceBagFrag)
        subTotalPriceBagFrag = view.findViewById(R.id.subTotalPriceBagFrag)
        checkOut_BagPage = view.findViewById(R.id.checkOut_BagPage)
        etVoucher = view.findViewById(R.id.etVoucher)
        applyVoucher = view.findViewById(R.id.applyVoucher)
        val bottomCartLayout: LinearLayout = view.findViewById(R.id.bottomCartLayout)
        val emptyBagMsgLayout: LinearLayout = view.findViewById(R.id.emptyBagMsgLayout)
        val MybagText: TextView = view.findViewById(R.id.MybagText)
        item = arrayListOf()


        animationView.playAnimation()
        animationView.loop(true)
        bottomCartLayout.visibility = View.GONE
        MybagText.visibility = View.GONE
        emptyBagMsgLayout.visibility = View.VISIBLE

        cartRecView.layoutManager = LinearLayoutManager(context)
        cartAdapter = CartAdapter(activity as Context, this)
        cartRecView.adapter = cartAdapter

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        saleViewModel = ViewModelProviders.of(this).get(SaleViewModel::class.java)

        discount = 0
        cartViewModel.product.observe(viewLifecycleOwner, Observer { List ->
            List?.let {
                cartAdapter.updateList(it)
                item.clear()
                sum = 0
                subTotal = 0
                total = 0
                item.addAll(it)
            }
            countDetail = 0

            if (List.size == 0) {
                animationView.playAnimation()
                animationView.loop(true)
                bottomCartLayout.visibility = View.GONE
                MybagText.visibility = View.GONE
                emptyBagMsgLayout.visibility = View.VISIBLE

            } else {
                emptyBagMsgLayout.visibility = View.GONE
                bottomCartLayout.visibility = View.VISIBLE
                MybagText.visibility = View.VISIBLE
                animationView.pauseAnimation()
            }

            item.forEach {
                if (it.isCheck) {
                    sum += it.subTotal
                    countDetail++
                }
            }
            totalPriceBagFrag.text = "Rp " + sum

            subTotalPriceBagFrag.text = "Rp " + sum.toString()
            discountTotalPriceBagFrag.text = "Rp " + discount.toString()
            totalPriceBagFrag.text = "Rp " + sum.toString()
        })

        cartViewModel.getProductUnPaidByUser(firebaseAuth.uid.toString())

        checkOut_BagPage.setOnClickListener {


            val epochSeconds = System.currentTimeMillis() / 1000
            val currentDate = Date()
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val formattedDateTime = formatter.format(currentDate)
            if (total != 0) {
                sum = total
            }

            var sale = SaleEntity(
                epochSeconds.toString(),
                firebaseAuth.uid.toString(),
                formattedDateTime,
                sum.toDouble(),
                "",
                "",
                "",
                StringUtils.orderStatusList[0],
                discount.toDouble(),
                0.0,
                System.currentTimeMillis().toString(),
                countDetail.toString(),
                "",
                ""
            )

            val intent = Intent(context, CheckoutActivity::class.java)
            intent.putExtra("sale", Gson().toJson(sale))
            intent.putExtra("item", Gson().toJson(item))
            startActivity(intent)
        }

        applyVoucher.setOnClickListener {
            var voucherCode = etVoucher.text.toString()
            var voucherPrice = StringUtils.voucherMap[voucherCode]
            if (voucherCode.isEmpty() || voucherPrice == null) {
                requireActivity().toast("Kupon tidak ditemukan")
            } else {
                discountTotalPriceBagFrag.text = voucherPrice.toString()
                discount = voucherPrice
                if (sum >= discount)
                    total = sum - discount
                else total = 0

                subTotalPriceBagFrag.text = "Rp " + sum.toString()
                totalPriceBagFrag.text = "Rp " + total.toString()
            }
        }

        return view
    }

    override fun onItemDeleteClick(product: ProductEntity) {
        cartViewModel.deleteCart(product)
        Toast.makeText(context, "Removed From Bag", Toast.LENGTH_SHORT).show()
    }

    override fun onItemUpdateClick(product: ProductEntity) {
        cartViewModel.updateCart(product)
    }


}