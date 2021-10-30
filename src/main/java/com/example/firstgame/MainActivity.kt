package com.example.firstgame

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title =  "Barber App"
Toast.makeText(this, "Firebase Connection Successful", Toast.LENGTH_SHORT).show()




        barberButton.setOnClickListener{
            startActivity(Intent(this, barberLogin::class.java))
        }

        clientButton.setOnClickListener{
            startActivity(Intent(this, clientWelcome::class.java))
        }

        fab.setOnClickListener{
            startActivity(Intent(this, userguide::class.java))
        }




    }



}
