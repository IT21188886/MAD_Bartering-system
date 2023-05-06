package com.example.realtimedatabasekotlin

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.realtimedatabasekotlin.databinding.ActivityEditDatasBinding
import com.example.realtimedatabasekotlin.databinding.ActivityUser1Binding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class editDatasUserSwap : AppCompatActivity() {
    private lateinit var binding: ActivityUser1Binding
    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUser1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imagechangeBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        val empID = intent.getStringExtra("id")
        setValuesToViews()

        binding.swapBtn.setOnClickListener {
dataadding()
val intent=Intent(this,viewActivityUserSwap::class.java)
            startActivity(intent)
        }
        binding.cancelBtn.setOnClickListener {


        val intent=Intent(this,viewActivityUserSwap::class.java)
            startActivity(intent)

        }
    }
    var selectedImageUri: Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            binding.imgDoctor.setImageDrawable(null)
            selectedImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            val bitmapDrawable = BitmapDrawable(bitmap)

            binding.imgDoctor.setBackgroundDrawable(bitmapDrawable)

        }
    }
    private fun setValuesToViews() {
        val empID=intent.getStringExtra("id")
        binding.nameTxt.setText(intent.getStringExtra("name"))
       binding.addressTxt.setText(intent.getStringExtra("districtOfProduct"))
        binding.pricext.setText(intent.getStringExtra("priceOfProduct"))
        val imageUri=intent.getStringExtra("image")

        Glide.with(this)
            .load(imageUri)
            .into(binding.productImage)

    }
private fun dataadding(){
    val swapId = intent.getStringExtra("id")
    val productId = intent.getStringExtra("productId")
    setValuesToViews()
    val name = binding.nameTxt.text.toString()
    val price = binding.pricext.text.toString()
    val address = binding.addressTxt.text.toString()
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = current.format(formatter)

    if (swapId != null) {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")
        ref.putFile(selectedImageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Toast.makeText(this, "Photo saved", Toast.LENGTH_SHORT).show()
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        val image = uri.toString()
                        updateData(productId!!,swapId, name, price, address, date, image)
                        Toast.makeText(this, "Successfuly Updated", Toast.LENGTH_SHORT).show()
                    }
            }}
}
    private fun updateData(
        id:String,
        swapid:String,
        name: String,
        Price: String,
        address: String,
        date: String,
        image:String,
    ) {
        val categoryText=intent.getStringExtra("category")

        database = FirebaseDatabase.getInstance().getReference("swap")
        val user = mapOf<String, String>(
            "id" to id,
            "swapId" to swapid,
            "name" to name,
            "nameOfProduct" to name,
            "priceOfProduct" to Price,
            "districtOfProduct" to address,
            "dateOfProduct" to date,
            "image" to image
        )

        database.child(id).updateChildren(user).addOnSuccessListener {

            binding.nameTxt.text.clear()
            binding.pricext.text.clear()
            binding.addressTxt.text.clear()




        }.addOnFailureListener {

            Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()

        }
    }
}