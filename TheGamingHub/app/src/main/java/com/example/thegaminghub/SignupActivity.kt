package com.example.thegaminghub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.thegaminghub.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    lateinit var signupBinding: ActivitySignupBinding

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view = signupBinding.root
        setContentView(view)

        signupBinding.buttonCreateAccount.setOnClickListener {

            val userEmail = signupBinding.EmailSignUp.text.toString()
            val userPassword = signupBinding.PasswordSignUp.text.toString()

            validateData(userEmail,userPassword)


        }

    }

    fun storeUserData(){

//        val data = UserModel(
//            email = signupBinding.EmailSignUp.text.toString()
//        )

        var email = signupBinding.EmailSignUp.text.toString()

        Firebase.database.reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid!!).setValue(email)

    }

    fun validateData(userEmail: String, userPassword: String){
        if(signupBinding.EmailSignUp.text.toString().isEmpty()||
            signupBinding.PasswordSignUp.text.toString().isEmpty()) {
            Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show()

        }else{
            signupWithEmail(userEmail,userPassword)
        }
    }

    fun signupWithEmail( userEmail: String, userPassword: String ) {

        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->

            if (task.isSuccessful){

                storeUserData()
                Toast.makeText(applicationContext,"Your account has been created", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignupActivity,Login::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(applicationContext,task.exception?.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
}