package com.example.buynow.presentation.user.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.buynow.R
import com.example.buynow.data.local.room.sale.SaleEntity
import com.example.buynow.data.local.room.sale.SaleViewModel
import com.example.buynow.presentation.user.adapter.SaleAdapter
import com.example.buynow.presentation.user.adapter.SaleItemClickAdapter
import com.google.firebase.auth.FirebaseAuth


class RecentFragment : Fragment(), SaleItemClickAdapter {

    private lateinit var cv1: CardView
    private lateinit var tv1: TextView
    private lateinit var cv2: CardView
    private lateinit var tv2: TextView
    private lateinit var cv3: CardView
    private lateinit var tv3: TextView
    private lateinit var myRecentText: TextView
    private lateinit var emptyBagMsgLayout: LinearLayout
    private lateinit var saleAdapter: SaleAdapter
    private lateinit var recentRecView: RecyclerView
    lateinit var animationView: LottieAnimationView
    lateinit var saleEntity: ArrayList<SaleEntity>
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var saleViewModel: SaleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recent, container, false)

        cv1 = view.findViewById(R.id.cv1)
        tv1 = view.findViewById(R.id.tv1)
        cv2 = view.findViewById(R.id.cv2)
        tv2 = view.findViewById(R.id.tv2)
        cv3 = view.findViewById(R.id.cv3)
        tv3 = view.findViewById(R.id.tv3)
        recentRecView = view.findViewById(R.id.recentRecView)
        myRecentText = view.findViewById(R.id.myRecentText)
        emptyBagMsgLayout = view.findViewById(R.id.emptyBagMsgLayout)
        animationView = view.findViewById(R.id.animationViewFavPage)

        saleEntity = arrayListOf()
        recentRecView.layoutManager = LinearLayoutManager(context)
        saleAdapter = SaleAdapter(activity as Context, this)
        recentRecView.adapter = saleAdapter

        saleViewModel = ViewModelProviders.of(this).get(SaleViewModel::class.java)

        saleViewModel.sales.observe(viewLifecycleOwner) { List ->
            List?.let {
                saleAdapter.updateList(it)
            }

            if (List.size == 0) {
                animationView.playAnimation()
                animationView.loop(true)
                emptyBagMsgLayout.visibility = View.VISIBLE

            } else {
                emptyBagMsgLayout.visibility = View.GONE
                animationView.pauseAnimation()
            }
        }

        cv1.setOnClickListener({
            cv1.setCardBackgroundColor(Color.GREEN)
            tv1.setTextColor(Color.WHITE)

            cv2.setCardBackgroundColor(Color.WHITE)
            tv2.setTextColor(Color.BLACK)
            cv3.setCardBackgroundColor(Color.WHITE)
            tv3.setTextColor(Color.BLACK)
            saleViewModel.getByUserID(firebaseAuth.uid.toString())


        })

        cv2.setOnClickListener({
            cv2.setCardBackgroundColor(Color.GREEN)
            tv2.setTextColor(Color.WHITE)

            cv1.setCardBackgroundColor(Color.WHITE)
            tv1.setTextColor(Color.BLACK)
            cv3.setCardBackgroundColor(Color.WHITE)
            tv3.setTextColor(Color.BLACK)
            saleViewModel.getByUserIdAndStatus(
                firebaseAuth.uid.toString(),
                "Pesanan Sedang Diproses"
            )

        })

        cv3.setOnClickListener({
            cv3.setCardBackgroundColor(Color.GREEN)
            tv3.setTextColor(Color.WHITE)

            cv1.setCardBackgroundColor(Color.WHITE)
            tv1.setTextColor(Color.BLACK)
            cv2.setCardBackgroundColor(Color.WHITE)
            tv2.setTextColor(Color.BLACK)
            saleViewModel.getByUserIdAndStatus(
                firebaseAuth.uid.toString(),
                "Pesanan Sedang Terkirim"
            )
        })

        saleViewModel.getByUserID(firebaseAuth.uid.toString())


        animationView.playAnimation()
        animationView.loop(true)

        return view
    }

    override fun onItemDeleteClick(saleEntity: SaleEntity) {
    }

    override fun onItemUpdateClick(saleEntity: SaleEntity) {

    }


}