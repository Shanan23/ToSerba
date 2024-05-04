package com.example.buynow.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.buynow.R
import com.example.buynow.data.model.Product
import com.example.buynow.utils.FirebaseUtils
import com.example.buynow.utils.StringUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val itemCollectionRef = Firebase.firestore.collection("Items")
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var coverProduct: ArrayList<Product>
    lateinit var newProduct: ArrayList<Product>
    lateinit var saleProduct: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        Handler().postDelayed({

            CoroutineScope(Dispatchers.IO).launch { syncItem() }

            checkUser()

        }, 1000)

    }

    private fun checkUser() {
        /*
        if(FirebaseUtils.firebaseUser?.isEmailVerified == true){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
*/
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
        if (firebaseAuth.uid != null) {
            setCoverData()
            setNewProductData()
        }

    }

    private suspend fun setCoverData() {

        val jsonFileString = applicationContext?.let {

            StringUtils.getJsonData(it, "CoverProducts.json")
        }
        val gson = Gson()

        val listCoverType = object : TypeToken<List<Product>>() {}.type

        var coverD: List<Product> = gson.fromJson(jsonFileString, listCoverType)

        coverD.forEachIndexed { idx, person ->

            coverProduct.add(person)
            saleProduct.add(person)
            itemCollectionRef.document(firebaseAuth.uid.toString()).set(saleProduct).await()

        }
    }

    private fun setNewProductData() {

        val jsonFileString = applicationContext?.let {

            StringUtils.getJsonData(it, "NewProducts.json")
        }
        val gson = Gson()

        val listCoverType = object : TypeToken<List<Product>>() {}.type

        var coverD: List<Product> = gson.fromJson(jsonFileString, listCoverType)

        coverD.forEachIndexed { idx, person ->


            newProduct.add(person)


        }


    }
}