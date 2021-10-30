package com.example.firstgame

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_client_welcome.*
import kotlinx.android.synthetic.main.fragment_profile.*


class barberDashboard : AppCompatActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barber_dashboard)
        val profileName= intent.getStringExtra("Username")
        supportActionBar?.title =  "Barber"

        val acceptedApp= intent.getCharArrayExtra("Appointments")

        replaceFragment(ProfileFragment.newInstance(profileName))








        val navigationView = findViewById<View>(R.id.nav) as BottomNavigationView

        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                 R.id.profile ->{

                     replaceFragment(ProfileFragment.newInstance(profileName))
                 }


                R.id.availability ->{
                    replaceFragment(AvailabilityFragment.newInstance(profileName))
                }

                R.id.appointments ->{
                    replaceFragment(AppointmentsFragment())
                }


            }
            true

        }


    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()

    }
}