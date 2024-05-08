package com.example.buynow.presentation.user.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.buynow.R
import com.example.buynow.utils.FirebaseUtils
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EditProfileActivity : AppCompatActivity() {

    private lateinit var docData: HashMap<String, String>
    private val llTitle: LinearLayout by lazy { findViewById<LinearLayout>(R.id.llTitle) }
    private val lblName: TextView by lazy { findViewById<TextView>(R.id.lblName) }
    private val etName: EditText by lazy { findViewById<EditText>(R.id.etName) }
    private val lblCallName: TextView by lazy { findViewById<TextView>(R.id.lblCallName) }
    private val etCallName: EditText by lazy { findViewById<EditText>(R.id.etCallName) }
    private val lblEmail: TextView by lazy { findViewById<TextView>(R.id.lblEmail) }
    private val etEmail: EditText by lazy { findViewById<EditText>(R.id.etEmail) }
    private val lblPhone: TextView by lazy { findViewById<TextView>(R.id.lblPhone) }
    private val etPhone: EditText by lazy { findViewById<EditText>(R.id.etPhone) }
    private val lblAddress: TextView by lazy { findViewById<TextView>(R.id.lblAddress) }
    private val etAddress: EditText by lazy { findViewById<EditText>(R.id.etAddress) }
    private val lblAddressShipment: TextView by lazy { findViewById<TextView>(R.id.lblAddressShipment) }
    private val etAddressShipment: EditText by lazy { findViewById<EditText>(R.id.etAddressShipment) }
    private val v1: View by lazy { findViewById<View>(R.id.v1) }
    private val btnSave: Button by lazy { findViewById<Button>(R.id.btnSave) }
    private val userCollectionRef = Firebase.firestore.collection("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        getUserData()

        btnSave.setOnClickListener {
            docData["userName"] = etName.text.toString()
            docData["userCallName"] = etCallName.text.toString()
            docData["userEmail"] = etEmail.text.toString()
            docData["userPhone"] = etPhone.text.toString()
            docData["userAddress"] = etAddress.text.toString()
            docData["userShipmentAddress"] = etAddressShipment.text.toString()

            userCollectionRef
                .document(FirebaseUtils.firebaseAuth.uid.toString())
                .set(docData, SetOptions.merge())

            finish()
        }
    }

    private fun getUserData() = CoroutineScope(Dispatchers.IO).launch {
        try {

            val querySnapshot = userCollectionRef
                .document(FirebaseUtils.firebaseAuth.uid.toString())
                .get().await()


            val userName: String = querySnapshot.data?.get("userName").toString()
            val userCallName: String = querySnapshot.data?.get("userCallName").toString()
            val userEmail: String = querySnapshot.data?.get("userEmail").toString()
            val userPhone: String = querySnapshot.data?.get("userPhone").toString()
            val userAddress: String =
                querySnapshot.data?.get("userAddress").toString()
            val userShipmentAddress: String =
                querySnapshot.data?.get("userShipmentAddress").toString()


            docData = hashMapOf(
                "userName" to userName,
                "userCallName" to userCallName,
                "userEmail" to userEmail,
                "userPhone" to userPhone,
                "userAddress" to userAddress,
                "userShipmentAddress" to userShipmentAddress,
            )

            withContext(Dispatchers.Main) {
                etName.setText(userName)
                etCallName.setText(userCallName)
                etPhone.setText(userPhone)
                etEmail.setText(userEmail)
                etAddress.setText(userAddress)
                etAddressShipment.setText(userShipmentAddress)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}