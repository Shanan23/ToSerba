package com.example.buynow.presentation.seller.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.buynow.R
import com.example.buynow.data.local.room.item.ItemEntity
import com.example.buynow.data.local.room.item.ItemViewModel
import com.example.buynow.data.model.Product
import com.example.buynow.utils.FirebaseUtils
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

class AddItemActivity : AppCompatActivity() {

    private var bitmap: Bitmap? = null
    private lateinit var itemViewModel: ItemViewModel
    private var maxId: Int = 1
    private lateinit var docData: HashMap<String, String>
    private val llTitle: LinearLayout by lazy { findViewById<LinearLayout>(R.id.llTitle) }
    private val lblName: TextView by lazy { findViewById<TextView>(R.id.lblName) }
    private val etName: EditText by lazy { findViewById<EditText>(R.id.etName) }
    private val lblCategory: TextView by lazy { findViewById<TextView>(R.id.lblCategory) }
    private val etCategory: EditText by lazy { findViewById<EditText>(R.id.etCategory) }
    private val lblPrice: TextView by lazy { findViewById<TextView>(R.id.lblPrice) }
    private val etPrice: EditText by lazy { findViewById<EditText>(R.id.etPrice) }
    private val lblStock: TextView by lazy { findViewById<TextView>(R.id.lblStock) }
    private val etStock: EditText by lazy { findViewById<EditText>(R.id.etStock) }
    private val lblImage: TextView by lazy { findViewById<TextView>(R.id.lblImage) }
    private val etImage: EditText by lazy { findViewById<EditText>(R.id.etImage) }
    private val btnUpload: Button by lazy { findViewById<Button>(R.id.btnUpload) }
    private val v1: View by lazy { findViewById<View>(R.id.v1) }
    private val btnSave: Button by lazy { findViewById<Button>(R.id.btnSave) }

    private val userCollectionRef = Firebase.firestore.collection("Users")
    private val itemCollectionRef = Firebase.firestore.collection("Items")
    private val PICK_IMAGE_REQUEST = 71
    lateinit var product: Product
    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)


        btnUpload.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, etImage)

            popupMenu.menuInflater.inflate(R.menu.profile_photo_storage, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.galleryMenu ->
                        launchGallery()

                    R.id.cameraMenu ->
                        dispatchTakePictureIntent()
                }
                true
            })
            popupMenu.show()
        }

        itemCollectionRef
            .orderBy("productId", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val maxIdDocument = documents.documents[0]
                    Log.d("maxIdDocument", maxIdDocument.id)

                    maxId = maxIdDocument?.data?.get("productId")?.toString()?.toInt()!! + 1

                    // Do something with maxId
                } else {
                    // Collection is empty
                }
            }
            .addOnFailureListener { exception ->
                // Handle failures
                exception.printStackTrace()
            }

        btnSave.setOnClickListener {
            val productName = etName.text.toString().trim()
            val productPrice = etPrice.text.toString().trim()
            val productCategory = etCategory.text.toString().trim()
            val productStock = etStock.text.toString().trim()
            val productImage = etImage.text.toString().trim()

            if (productName.isEmpty() || productPrice.isEmpty() || productCategory.isEmpty() ||
                productStock.isEmpty()
            ) {
                // At least one of the fields is empty
                // Handle the case where one or more fields are empty
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("MaxId", (maxId).toString())

                docData = hashMapOf()
                docData["productName"] = productName
                docData["productPrice"] = productPrice
                docData["productCategory"] = productCategory
                docData["productStock"] = productStock
                docData["productImage"] = productImage
                docData["productBrand"] = ""
                docData["productDes"] = ""
                docData["productDisCount"] = ""
                docData["productFav"] = ""
                docData["productHave"] = ""
                docData["productId"] = (maxId).toString()
                docData["productNote"] = ""
                docData["productRating"] = "3"
                docData["productUserId"] = FirebaseUtils.firebaseAuth.uid.toString()

                itemCollectionRef
                    .document(maxId.toString())
                    .set(docData)

                uploadImage()
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage() {

        if (filePath != null) {
            val ref = FirebaseUtils.storageReference.child(
                "items/" + UUID.randomUUID().toString()
            )

            val uploadTask = ref.putFile(filePath!!)

            val urlTask =
                uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        Log.d("SQL MaxId", (maxId).toString())

                        addUploadRecordToDb(downloadUri.toString(), (maxId).toString())

                        itemCollectionRef.orderBy("productId", Query.Direction.DESCENDING).get()
                            .addOnCompleteListener(OnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!) {
                                        // Convert each document to your data class and add to ArrayList
                                        val person = document.toObject(ItemEntity::class.java)

                                        Log.d("SQL Add Item: ", person.toString())

                                        itemViewModel.insertItem(person)


                                    }
                                } else {
                                    // Handle errors here
                                }
                            })

                    } else {
                        // Handle failures
                    }
                }?.addOnFailureListener {

                }
        } else {

            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                etImage.setText(filePath?.path)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addUploadRecordToDb(uri: String, cartId: String) =
        CoroutineScope(Dispatchers.IO).launch {

            try {

                Log.d("productImage", uri);

                itemCollectionRef.document(cartId)
                    .update("productImage", uri).await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddItemActivity, "Saved", Toast.LENGTH_SHORT).show()
                    finish()

                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AddItemActivity,
                        "" + e.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, PICK_IMAGE_REQUEST)
            }
        }
    }
}