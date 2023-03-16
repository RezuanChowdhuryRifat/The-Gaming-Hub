package com.example.thegaminghub

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menuSignOut){

            showDialogMessage()

        }

        return super.onOptionsItemSelected(item)
    }

     fun showDialogMessage() {

        val dialogMessage = AlertDialog.Builder(this)
         dialogMessage.setTitle("Sign out")
         dialogMessage.setMessage("Do you want to sign out?")
         dialogMessage.setCancelable(false)
         dialogMessage.setNegativeButton("Cancel",DialogInterface.OnClickListener{ dialogInterface, i ->
                 dialogInterface.cancel()
             })

         dialogMessage.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->

                 FirebaseAuth.getInstance().signOut()
                 val intent = Intent(this@MainActivity, Login::class.java)
                 startActivity(intent)
                 finish()
             })
             dialogMessage.create().show()


    }
}