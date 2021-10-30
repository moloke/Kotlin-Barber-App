package com.example.firstgame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_barber_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class barberLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barber_login)
        supportActionBar?.title =  "Barber Login"



        barberLoginButton.setOnClickListener{
            if ((barberUsername.text.isEmpty()) || (barberPassword.text.isEmpty())) {
                if(barberUsername.text.isEmpty()){
                    barberUsername.error = "Username required"
                } else if (barberPassword.text.isEmpty()) {
                    barberPassword.error = "Password required"
                }

            } else if ((barberUsername.text.toString() == "ee") && (barberPassword.text.toString() == "ee")){
                val intent = Intent(this@barberLogin,barberDashboard::class.java)
                intent.putExtra("Username",barberUsername.text.toString())
                startActivity(intent)

            } else {
                val text = "Invalid Username or Password"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.setGravity(Gravity.TOP, 0, 220)
                toast.show()
                barberPassword.setText("")
            }


        }






    }
}


