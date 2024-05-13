package com.example.buynow.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.buynow.R
import com.example.buynow.data.local.room.item.ItemEntity
import com.example.buynow.data.local.room.item.ItemViewModel
import com.example.buynow.data.model.Product
import com.example.buynow.presentation.user.activity.HomeActivity
import com.example.buynow.utils.FirebaseUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var itemViewModel: ItemViewModel
    private val itemCollectionRef = Firebase.firestore.collection("Items")
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var coverProduct: ArrayList<Product>
    lateinit var newProduct: ArrayList<Product>
    lateinit var saleProduct: ArrayList<Product>

    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        checkPermission()
    }

    private fun checkPermission() {
        // Check camera permission
        val cameraPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        // Check write external storage permission
        val storagePermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (cameraPermissionGranted && storagePermissionGranted) {
            initializeAndSyncItems()
        } else {
            // Request permissions from the user or close the app
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allPermissionsGranted) {
                initializeAndSyncItems()
            } else {
                // Permissions not granted, close the app
                checkPermission()

            }
        }
    }

    private fun initializeAndSyncItems() {
        coverProduct = arrayListOf()
        newProduct = arrayListOf()
        saleProduct = arrayListOf()
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        CoroutineScope(Dispatchers.IO).launch {                     checkUser()
            checkUser() }
    }

    private fun checkUser() {
        if (FirebaseUtils.firebaseUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (FirebaseUtils.firebaseUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private suspend fun syncItem() {
        setCoverData()
    }

    private suspend fun setCoverData() {

        itemCollectionRef.orderBy("name", Query.Direction.DESCENDING).get()
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        // Convert each document to your data class and add to ArrayList
                        val person = document.toObject(ItemEntity::class.java)

                        Log.d("SQL Query: ", person.toString())

                        itemViewModel.insertItem(person)
                    }

                    checkUser()

                    // Now you can use your ArrayList (dataList) as needed
                    // For example, you can pass it to an adapter for RecyclerView
                } else {
                    // Handle errors here
                }
            })

    }
}