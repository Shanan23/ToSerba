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
import com.example.buynow.utils.Extensions.toast
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
    private val categoryCollectionRef = Firebase.firestore.collection("Categories")
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
        CoroutineScope(Dispatchers.IO).launch {
            syncItem()
        }
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
        if (FirebaseUtils.firebaseUser != null) {
            itemCollectionRef.orderBy("productId", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            // Convert each document to your data class and add to ArrayList
//                                    val person = document.toObject(ItemEntity::class.java)
                            var stock = 0
                            if (!document.data["productStock"].toString().equals("null")) {
                                stock = document.data["productStock"].toString().toInt()
                            }
                            var itemEntity = ItemEntity(
                                document.data["productId"].toString().toInt(),
                                document.data["productUserId"].toString(),
                                document.data["productName"].toString(),
                                document.data["productPrice"].toString().replace(Regex("\\D"), "")
                                    .toInt(),
                                document.data["productImage"].toString(),
                                document.data["productDes"].toString(),
                                document.data["productRating"].toString()
                                    .toDouble(),
                                document.data["productDisCount"].toString(),
                                stock,
                                false,
                                document.data["productBrand"].toString(),
                                document.data["productCategory"].toString(),
                                document.data["productNote"].toString()
                            )

                            Log.d("SQL Query: ", itemEntity.toString())

                            itemViewModel.insertItem(itemEntity)

                            var docData: HashMap<String, String> = hashMapOf()
                            docData["productCategory"] =
                                document.data["productCategory"].toString().uppercase()

                            categoryCollectionRef
                                .document(document.data["productCategory"].toString().uppercase())
                                .set(docData)
                        }

                        checkUser()

                    } else {
                        // Handle errors here
                        toast("Sync item failed")
                    }
                })
        } else {
            checkUser()
        }
    }
}