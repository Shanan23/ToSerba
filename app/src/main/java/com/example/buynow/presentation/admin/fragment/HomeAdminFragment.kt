package com.example.buynow.presentation.admin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buynow.R
import com.example.buynow.data.local.room.AppDatabase
import com.example.buynow.data.local.room.SummaryViewModel
import com.example.buynow.data.local.room.SummaryViewModelFactory
import com.example.buynow.data.local.room.category.CategoryViewModel
import com.example.buynow.data.local.room.item.ItemEntity
import com.example.buynow.data.local.room.item.ItemViewModel
import com.example.buynow.data.model.SummaryAdmin
import com.example.buynow.presentation.admin.adapter.ProductAdminAdapter
import com.example.buynow.presentation.admin.adapter.SummaryAdminAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeAdminFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var saleProduct: ArrayList<ItemEntity>
    lateinit var summaryAdmins: List<SummaryAdmin>
    private lateinit var itemViewModel: ItemViewModel
    private lateinit var summaryViewModel: SummaryViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    lateinit var etSearch: EditText
    lateinit var ibFilter: ImageButton
    private lateinit var profileImage_profileFrag: CircleImageView
    lateinit var tvGreetingName: TextView
    lateinit var adminProductRecView: RecyclerView
    lateinit var adminSummaryRecView: RecyclerView
    lateinit var productAdminAdapter: ProductAdminAdapter
    lateinit var summaryAdminAdapter: SummaryAdminAdapter

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userCollectionRef = Firebase.firestore.collection("Users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_admin, container, false)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        profileImage_profileFrag = view.findViewById(R.id.profileImage_profileFrag)
        tvGreetingName = view.findViewById(R.id.tvGreetingName)
        adminProductRecView = view.findViewById(R.id.adminProductRecView)
        adminSummaryRecView = view.findViewById(R.id.adminSummaryRecView)
        etSearch = view.findViewById(R.id.etSearch)
        ibFilter = view.findViewById(R.id.ibFilter)

        saleProduct = arrayListOf()
        summaryAdmins = arrayListOf()
        getItems()

        adminProductRecView.layoutManager = LinearLayoutManager(requireContext())
        adminProductRecView.setHasFixedSize(true)
        adminSummaryRecView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adminSummaryRecView.setHasFixedSize(true)

        productAdminAdapter = ProductAdminAdapter(saleProduct, requireContext())
        adminProductRecView.adapter = productAdminAdapter

        ibFilter.setOnClickListener {
            productAdminAdapter.filter.filter(etSearch.text.toString())
        }

        getUserData()
        return view
    }

    private fun getUserData() = CoroutineScope(Dispatchers.IO).launch {
        try {

            val querySnapshot = userCollectionRef
                .document(firebaseAuth.uid.toString())
                .get().await()

            val userImage: String = querySnapshot.data?.get("userImage").toString()
            val userName: String = querySnapshot.data?.get("userName").toString()

            withContext(Dispatchers.Main) {

                Glide.with(requireContext())
                    .load(userImage)
                    .placeholder(R.drawable.ic_profile)
                    .into(profileImage_profileFrag)
            }
        } catch (e: Exception) {

        }
    }

    private fun getItems() {
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        val itemDao = AppDatabase.getInstance(requireContext()).itemDao()
        val categoryDao = AppDatabase.getInstance(requireContext()).categoryDao()

        val viewModelFactory = SummaryViewModelFactory(requireContext(), itemDao, categoryDao)

        summaryViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SummaryViewModel::class.java)

        itemViewModel.allItems.observe(viewLifecycleOwner, Observer { List ->
            List?.let {
                var itemSize = it.size
                productAdminAdapter.updateList(it)
            }
        })

        summaryViewModel.combinedData.observe(viewLifecycleOwner, Observer { combinedData ->
            // Update RecyclerView adapter with combinedData
            summaryAdminAdapter = SummaryAdminAdapter(combinedData, requireContext())
            adminSummaryRecView.adapter = summaryAdminAdapter
        })
    }
}