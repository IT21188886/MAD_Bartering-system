package com.example.realtimedatabasekotlin

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.realtimedatabasekotlin.databinding.ActivityUser1Binding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class userActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivityUser1Binding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUser1Binding.inflate(layoutInflater)
        setContentView(binding.root)


        val productName = intent.getStringExtra("name")
        val productAddress = intent.getStringExtra("districtOfProduct")
        val productPrice = intent.getStringExtra("priceOfProduct")

        val imageUri = intent.getStringExtra("image")
        Glide.with(this)
            .load(imageUri)
            .into(binding.productImage)
        binding.nameView.text = productName
        binding.addressView.text = productAddress
        binding.priceView.text = productPrice
        binding.imagechangeBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        binding.swapBtn.setOnClickListener {
            adddata()
        }

    }

    var selectedImageUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            binding.imgDoctor.setBackgroundDrawable(bitmapDrawable)

        }


    }

    private fun adddata() {
        val productId = intent.getStringExtra("id")
        val productName = binding.nameTxt.text.trim().toString()
        Log.i("Firebase image URL", "date1")
        val price = binding.pricext.text.trim().toString() + ".Rs"
        Log.i("Firebase image URL", "date2")
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = current.format(formatter)
        Log.i("Firebase image URL", "date3")
        val address = binding.addressTxt.text.trim().toString()
        Log.i("Firebase image URL", "date4")
        var imgIdNew: String = ""
        Log.i("Firebase image URL", "date5")


        database = FirebaseDatabase.getInstance().getReference("Swap")
        Log.i("Firebase image URL", "dates7")
        val swapId = database.push().key!!
        Log.i("Firebase image URL", "date8")

        if (selectedImageUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")
        ref.putFile(selectedImageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Toast.makeText(this, "Wait.Saving Process will take some time.", Toast.LENGTH_SHORT)
                    .show()
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        imgIdNew = uri.toString()
                        Log.i("Firebase image URL", "$imgIdNew")
                        val product =
                            swapClass(productId,swapId, imgIdNew, productName, price, address, date)
                        database.child(swapId).setValue(product).addOnSuccessListener {


                            val intent = Intent(this@userActivity1, viewActivityUserSwap::class.java)

                            startActivity(intent)
                            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()

                        }.addOnFailureListener {

                            Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show()


                        }
                    }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to atttch image to database", Toast.LENGTH_SHORT)
                    .show()
            }
        Log.i("Firebase image URL", "$imgIdNew")


    }


}