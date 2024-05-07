package com.example.buynow.presentation.admin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buynow.R
import com.example.buynow.data.model.User
import com.example.buynow.presentation.admin.adapter.UserAdminAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserAdminFragment : Fragment() {

    lateinit var userAdminAdapter: UserAdminAdapter
    val userCollectionRef = Firebase.firestore.collection("Users")
    lateinit var adminUserRecView: RecyclerView
    lateinit var etSearch: EditText
    lateinit var ibFilter: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_admin, container, false)
        adminUserRecView = view.findViewById(R.id.adminUserRecView)
        etSearch = view.findViewById(R.id.etSearch)
        ibFilter = view.findViewById(R.id.ibFilter)

        getUserData()

        adminUserRecView.layoutManager = LinearLayoutManager(requireContext())
        adminUserRecView.setHasFixedSize(true)

        ibFilter.setOnClickListener {
            if (userAdminAdapter != null) {
                userAdminAdapter.filter.filter(etSearch.text.toString())
            }
        }
        return view
    }


    private fun getUserData() = CoroutineScope(Dispatchers.IO).launch {
        try {
            userCollectionRef
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var users: ArrayList<User> = arrayListOf()
                        for (document in task.result!!) {
                            var user = User()
                            user.userImage = document.data["userImage"].toString()
                            user.userName = document.data["userName"].toString()
                            user.userAddress = document.data["userAddress"].toString()
                            user.userPhone = document.data["userphinee"].toString()
                            users.add(user)
                        }

                        userAdminAdapter = UserAdminAdapter(users, requireContext())
                        adminUserRecView.adapter = userAdminAdapter
                    }
                }
        } catch (e: Exception) {

        }
    }
}