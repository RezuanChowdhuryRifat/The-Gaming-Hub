package com.example.thegaminghub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.thegaminghub.databinding.ActivityForgotPasswordBinding
import com.example.thegaminghub.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    lateinit var forgotPasswordBinding: ActivityForgotPasswordBinding

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = forgotPasswordBinding.root
        setContentView(view)

        forgotPasswordBinding.buttonResetPassword.setOnClickListener {

            val email = forgotPasswordBinding.textForgotEmail.text.toString()

            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext,"Check your email to reset password ",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    Toast.makeText(applicationContext,"Try again",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}