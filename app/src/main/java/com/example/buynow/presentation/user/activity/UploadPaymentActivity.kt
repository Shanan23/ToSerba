package com.example.buynow.presentation.user.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.buynow.R
import com.example.buynow.data.local.room.sale.SaleEntity
import com.example.buynow.data.local.room.sale.SaleViewModel
import com.example.buynow.utils.FirebaseUtils
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID

class UploadPaymentActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var saleViewModel: SaleViewModel
    private var saleEntity: SaleEntity? = null
    private var saleEntityJson: String? = null
    private val llTitle: LinearLayout by lazy { findViewById<LinearLayout>(R.id.llTitle) }
    private val lblName: TextView by lazy { findViewById<TextView>(R.id.lblName) }
    private val etName: EditText by lazy { findViewById<EditText>(R.id.etName) }
    private val lblPayDate: TextView by lazy { findViewById<TextView>(R.id.lblPayDate) }
    private val etPayDate: EditText by lazy { findViewById<EditText>(R.id.etPayDate) }
    private val lblPayUpload: TextView by lazy { findViewById<TextView>(R.id.lblPayUpload) }
    private val etPayUpload: EditText by lazy { findViewById<EditText>(R.id.etPayUpload) }
    private val lblPayment: TextView by lazy { findViewById<TextView>(R.id.lblPayment) }
    private val lblDana: TextView by lazy { findViewById<TextView>(R.id.lblDana) }
    private val btnSave: Button by lazy { findViewById<Button>(R.id.btnSave) }
    private val backIv_ProfileFrag: ImageView by lazy { findViewById<ImageView>(R.id.backIv_ProfileFrag) }
    private val imgDate: ImageView by lazy { findViewById<ImageView>(R.id.imgDate) }
    private val userCollectionRef = Firebase.firestore.collection("Sales")
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_payment)

        saleEntityJson = intent.getStringExtra("saleEntity")
        saleViewModel = ViewModelProviders.of(this).get(SaleViewModel::class.java)

        if (saleEntityJson != null) {
            saleEntity = Gson().fromJson(saleEntityJson, SaleEntity::class.java)
        }
        imgDate.setOnClickListener {
            showDatePickerDialog()
        }

        backIv_ProfileFrag.setOnClickListener {
            onBackPressed()
        }

        etPayUpload.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, etPayUpload)

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

        btnSave.setOnClickListener {
            if (etName.text.isEmpty()) {
                etName.error = "Nama tidak boleh kosong"
                return@setOnClickListener
            }
            if (etPayDate.text.isEmpty()) {
                etPayDate.error = "Tanggal tidak boleh kosong"
                return@setOnClickListener
            }
            if (filePath != null) {
                val currentDate = Date()
                val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                saleEntity?.payName = "Dana"
                saleEntity?.status = "Sudah Upload Bukti"
                saleEntity?.paidAt = formatter.format(currentDate)
                saleEntity?.let { it1 -> saleViewModel.insertSale(it1) }
                uploadImage()
            } else {
                Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
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
                "resi/" + UUID.randomUUID().toString()
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
                        if (saleEntityJson != null) {
                            var saleEntity = Gson().fromJson(saleEntityJson, SaleEntity::class.java)
                            addUploadRecordToDb(downloadUri.toString(), saleEntity.sId)
                        }

                        // show save...


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
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                etPayUpload.setText(filePath?.path)
                uploadImage()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addUploadRecordToDb(uri: String, cartId: String) =
        CoroutineScope(Dispatchers.IO).launch {

            try {

                userCollectionRef.document(cartId)
                    .update("imageResi", uri).await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UploadPaymentActivity, "Saved", Toast.LENGTH_SHORT).show()
                    finish()

                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@UploadPaymentActivity,
                        "" + e.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerFragment()
        datePickerDialog.show(supportFragmentManager, "datePicker")
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, PICK_IMAGE_REQUEST)
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Do something with the selected date
        val selectedDate = "$dayOfMonth/${month + 1}/$year"
        etPayDate.setText(selectedDate)
    }

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(requireActivity(), this, year, month, day)
        }

        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            // Do something with the selected date
            this.onDateSet(view, year, month, dayOfMonth)
        }
    }
}