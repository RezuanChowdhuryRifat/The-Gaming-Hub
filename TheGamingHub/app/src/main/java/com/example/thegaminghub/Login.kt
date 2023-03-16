package com.example.thegaminghub

import android.content.ContentProviderClient
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.example.thegaminghub.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    lateinit var loginBinding: ActivityLoginBinding
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        registerActivityForGoogleSignIn()
        loginBinding.buttonSignIn.setOnClickListener{

            val userEmail = loginBinding.EmailSignIn.text.toString()
            val userPassword = loginBinding.PasswordSignIn.text.toString()

            validateData(userEmail,userPassword)
        }

        loginBinding.buttonSignUp.setOnClickListener {

            val intent = Intent(this@Login,SignupActivity::class.java)
            startActivity(intent)
        }

        loginBinding.buttonForgotPassword.setOnClickListener {

            val intent = Intent(this,ForgotPassword::class.java)
            startActivity(intent)
        }

        loginBinding.googleSignIn.setOnClickListener {
            googleSignIn()

        }

    }

    ///GOOGLE SIGN IN START///
    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("493938860233-06l5adta8l8b0lmncfn8ovqhsj5u5snc.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        signIn()

    }

    private fun signIn() {

        val signIntent: Intent = googleSignInClient.signInIntent
        activityResultLauncher.launch(signIntent)

    }

    private fun registerActivityForGoogleSignIn(){

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->

                val resultCode = result.resultCode
                val data = result.data

                if(resultCode == RESULT_OK && data !=null){

                    val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                    firebaseSignWithGoogle(task)
                }
            })

    }

    private fun firebaseSignWithGoogle(task: Task<GoogleSignInAccount>) {

        try {
            val account : GoogleSignInAccount = task.getResult(ApiException::class.java)
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)


        }catch (e: ApiException){
            Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }

    }

    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {

        val authCredential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(authCredential).addOnCompleteListener {task->

            if(task.isSuccessful){

                val user = auth.currentUser
                storeUserData()

            }else{

            }
        }
    }

    fun storeUserData(){

        var email = auth.currentUser?.email.toString()

        Firebase.database.reference.child("users").child(auth.currentUser!!.uid!!).setValue(email)

    }
    ///GOOGLE SIGN IN END///


    fun validateData(userEmail: String, userPassword: String){
      if(loginBinding.EmailSignIn.text.toString().isEmpty()||
              loginBinding.PasswordSignIn.text.toString().isEmpty()) {
          Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show()

      }else{
          signinWithEmail(userEmail,userPassword)
      }
  }

  fun signinWithEmail(userEmail: String, userPassword: String){
      auth.signInWithEmailAndPassword(userEmail, userPassword)
          .addOnCompleteListener(this) { task ->
              if (task.isSuccessful) {

                  val intent = Intent(this@Login,MainActivity::class.java)
                  startActivity(intent)

              } else {

                  Toast.makeText(applicationContext,task.exception?.toString(),Toast.LENGTH_SHORT).show()

              }
          }
  }


    override fun onStart() {
        super.onStart()

        val user = auth.currentUser

        if (user != null){

            val intent = Intent(this@Login,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}