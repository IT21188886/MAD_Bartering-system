package com.example.realtimedatabasekotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realtimedatabasekotlin.databinding.ActivityUser1Binding
import com.example.realtimedatabasekotlin.databinding.ActivityViewDatas2Binding
import com.example.realtimedatabasekotlin.databinding.ActivityViewDatasBinding
import com.google.firebase.database.*
import java.util.*

class viewActivityUserSwap : AppCompatActivity() {
    private lateinit var binding: ActivityViewDatas2Binding

    private lateinit var dbref: DatabaseReference
    private lateinit var empRecyclerview: RecyclerView
    private lateinit var empList: ArrayList<swapClass>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityViewDatas2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        empRecyclerview = findViewById(R.id.doctorRecycleView)
        empRecyclerview.layoutManager = GridLayoutManager(this, 2)
        empRecyclerview.setHasFixedSize(true)
        empList = arrayListOf<swapClass>()
        getUserData()
        Toast.makeText(this,"Swap succesfull",Toast.LENGTH_LONG).show()
    }

    private fun getUserData() {

        val nameButton = intent.getStringExtra("name")
        var category: String = ""
        when (nameButton) {
            "Vehicle" -> {
                dbref = FirebaseDatabase.getInstance().getReference("Vehicle")
                category = "Vehicle"
            }
            "Properties" -> {
                dbref = FirebaseDatabase.getInstance().getReference("Property")
                category = "Property"
            }
            "Eectric Devices" -> {
                dbref = FirebaseDatabase.getInstance().getReference("Electronic")
                category = "Electronic"
            }
            "Fashion and cosmetics" -> {
                dbref = FirebaseDatabase.getInstance().getReference("Fashion")
                category = "Fashion"
            }
            "Sports" -> {
                dbref = FirebaseDatabase.getInstance().getReference("Sport")
                category = "Sport"
            }
            "Services" -> {
                dbref = FirebaseDatabase.getInstance().getReference("Sport")
                category = "Sport"
            }
            else -> {
                dbref = FirebaseDatabase.getInstance().getReference("else")
                category = "else"
            }
        }
        Log.i("Firebase image URL", "date8")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("Firebase image URL", "date8")
                if (snapshot.exists()) {
                    Log.i("Firebase image URL", "date8")
                    for (userSnapshot in snapshot.children) {

                        val doctor = userSnapshot.getValue(swapClass::class.java)
                        empList.add(doctor!!)

                    }
                    val mAdapter = productAdaptorSwap(empList, this@viewActivityUserSwap)
                    empRecyclerview.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : productAdaptorSwap.onItemClickListener {
                        override fun onItemClick(position: Int) {
                                val intent = Intent(this@viewActivityUserSwap, editDatasUserSwap::class.java)

                                intent.putExtra("name", empList[position].nameOfProduct)
                            val swapid = intent.getStringExtra("swapId")

                            intent.putExtra("id", empList[position].swapId)
                                intent.putExtra("priceOfProduct", empList[position].priceOfProduct)
                                intent.putExtra(
                                    "districtOfProduct",
                                    empList[position].districtOfProduct
                                )
                                intent.putExtra("image", empList[position].image)
                                intent.putExtra("productId", empList[position].id)

                                startActivity(intent)


                        }

                    })


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
}