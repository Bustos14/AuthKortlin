package com.example.authenticationkotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset.*


class ResetPassword : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_reset)
        rpButton.setOnClickListener {
            var a = firebaseAuth.sendPasswordResetEmail(resetPassword.text.toString())
        }
        backButton.setOnClickListener {
            val homeIntent = Intent(this, AuthActivity::class.java).apply {

            }
            startActivity(homeIntent)
        }

    }
}