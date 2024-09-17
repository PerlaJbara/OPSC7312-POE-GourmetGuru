package com.opsc7311poe.gourmetguru_opscpoe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var btnGoToReg: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btnLogin)
        btnGoToReg = findViewById(R.id.txtNoAccount)

        btnLogin.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnGoToReg.setOnClickListener(){
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
            finish()
        }

    }
}