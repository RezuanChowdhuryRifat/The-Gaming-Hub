package com.example.thegaminghub

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.thegaminghub.databinding.ActivityProfileBinding
import com.example.thegaminghub.databinding.ActivitySignupBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity(){

    lateinit var profileBinding: ActivityProfileBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        val view = profileBinding.root
        setContentView(view)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.profileNavbar
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeNavbar -> {
                    // Handle the Home item click
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.searchNavbar -> {
                    // Handle the Dashboard item click
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.reviewNavbar -> {
                    // Handle the Notifications item click
                    val intent = Intent(this, ReviewActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.profileNavbar -> {
                    // Handle the Notifications item click
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

       profileBinding.logout.setOnClickListener{
           showDialogMessage()
       }

        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email


        profileBinding.ProfileName.text = email.toString()




    }

    fun showDialogMessage() {

        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Sign out")
        dialogMessage.setMessage("Do you want to sign out?")
        dialogMessage.setCancelable(false)
        dialogMessage.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })

        dialogMessage.setPositiveButton(
            "Yes",
            DialogInterface.OnClickListener { dialogInterface, i ->

                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@ProfileActivity, Login::class.java)
                startActivity(intent)
                finish()
            })
        dialogMessage.create().show()


    }
}