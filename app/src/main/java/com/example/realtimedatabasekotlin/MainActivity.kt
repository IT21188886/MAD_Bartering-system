package com.example.realtimedatabasekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.realtimedatabasekotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val userName=binding.userName.text.toString()
            val password=binding.password.text.toString()
            when(userName+" "+password){
                "user password1"->{val intent = Intent(this, ViewDatasUser::class.java)
                    val value:String=userName+" "+password
                    intent.putExtra("access", value)
                    startActivity(intent)
                }
                "admin password2"->{val intent = Intent(this, ViewDatas::class.java)
                    val value:String=userName+" "+password
                    intent.putExtra("access", value)
                    startActivity(intent)}
                else->{Toast.makeText(this,"Incorrect credentials",Toast.LENGTH_SHORT).show()
                binding.userName.text=null
                    binding.password.text=null
                }
            }
        }
    }
}