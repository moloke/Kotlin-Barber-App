package com.example.firstgame

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_barber_info.*
import kotlinx.android.synthetic.main.bookdialog.*
import kotlinx.android.synthetic.main.bookdialog.view.*
import kotlinx.android.synthetic.main.fragment_availability.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.activity_barber_info.haircut1 as haircut11
import kotlinx.android.synthetic.main.activity_barber_info.haircut3 as haircut31
import kotlinx.android.synthetic.main.fragment_profile.haircut2 as haircut21
import kotlinx.android.synthetic.main.fragment_profile.profilePicShow as profilePicShow1
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory
import java.io.File
import java.io.FileOutputStream
import android.content.ActivityNotFoundException
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*


class barberInfo : AppCompatActivity() {

private var blocation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val barberSelected = intent.getStringExtra(clientSearch.BARBER_KEY)




        if (barberSelected == "ee") {
            setContentView(R.layout.activity_barber_info)
            supportActionBar?.title = barberSelected + " info"


            val numref = FirebaseDatabase.getInstance().getReference("/Barber Profiles").child("ee").child("profile")
            numref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val barberNumber = p0.child("number").getValue().toString()
                    val barberPostcode = p0.child("postcode").getValue().toString()
                    val barberDes = p0.child("description").getValue().toString()
                    numtxt.text = barberNumber
                    posttxt.text = barberPostcode
                    destxt.text = barberDes

                }
                override fun onCancelled(p0: DatabaseError) {}
            })







            bookBtn.setOnClickListener {
                //Inflate the dialog with custom view
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.bookdialog, null)
                //AlertDialogBuilder
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                    .setTitle("Booking Form")
                //show dialog
                val  mAlertDialog = mBuilder.show()


                //login button click of custom layout
                mDialogView.bookNowBtn.setOnClickListener {


                        //dismiss dialog
                        mAlertDialog.dismiss()
                        //get text from EditTexts of custom layout
                        val name = mDialogView.NameEt.text.toString()
                        val date = mDialogView.DateEt.text.toString()
                        val time = mDialogView.TimeEt.text.toString()
                        val address = mDialogView.AddressEt.text.toString()
                        val booking = Booking(name, date, time, address)


                        val ref = FirebaseDatabase.getInstance().getReference("Bookings")
                        ref.setValue(booking).addOnCompleteListener {
                            Toast.makeText(this, "Appointment pending", Toast.LENGTH_SHORT).show()
                        }


                }
                //cancel button click of custom layout
                mDialogView.CancelBtn.setOnClickListener {
                    //dismiss dialog
                    mAlertDialog.dismiss()
                }
            }

            val avref = FirebaseDatabase.getInstance().getReference("/Barber Profiles").child("ee").child("availability")
            avref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val avDates = p0.child("availability").getValue().toString()
                    availableDatesTxt.text = avDates
                }
                override fun onCancelled(p0: DatabaseError) {}
            })
            val ref = FirebaseDatabase.getInstance().getReference("/Barbers").child("ee")
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val img = p0.child("image").getValue().toString()
                    Picasso.get().load(img).into(profilePicShow)
                    profilePicShow.setBackgroundColor(Color.TRANSPARENT)
                }
                override fun onCancelled(p0: DatabaseError) {}
            })
            val reff = FirebaseDatabase.getInstance().getReference("/Barber Profiles").child("ee").child("haircut image1")
            reff.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val img = p0.child("profileImageUrl").getValue().toString()
                    Picasso.get().load(img).into(haircut1)
                }
                override fun onCancelled(p0: DatabaseError) {}
            })
            val refff = FirebaseDatabase.getInstance().getReference("/Barber Profiles").child("ee").child("haircut image2")
            refff.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val img = p0.child("profileImageUrl").getValue().toString()
                    Picasso.get().load(img).into(haircut2)
                }
                override fun onCancelled(p0: DatabaseError) {}
            })
            val reffff = FirebaseDatabase.getInstance().getReference("/Barber Profiles").child("ee").child("haircut image3")
            reffff.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val img = p0.child("profileImageUrl").getValue().toString()
                    Picasso.get().load(img).into(haircut3)
                }
                override fun onCancelled(p0: DatabaseError) {}
            })

            val acref = FirebaseDatabase.getInstance().getReference("/Accepted")
            acref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val accepted = p0.child("client1").getValue().toString()
                    if (accepted == "yes") {
                        Toast.makeText(this@barberInfo, "appointment accepted", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onCancelled(p0: DatabaseError) {}
            })


        } else {
            setContentView(R.layout.activity_dummy_barber_info)

        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.shareIcon -> {






                val locref = FirebaseDatabase.getInstance().getReference("/Barbers").child("ee")
                locref.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        blocation = p0.child("postcode").getValue().toString()

                    }
                    override fun onCancelled(p0: DatabaseError) {}
                })



                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                val sharebody = "Check out this new barber in $blocation!!"
                val sharesub = "sub here"

                intent.putExtra(Intent.EXTRA_SUBJECT, sharesub)
                intent.putExtra(Intent.EXTRA_TEXT, sharebody)
                startActivity(Intent.createChooser(intent, "Share barber"))
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    fun takeScreenshot(): Bitmap {
        val rootView = findViewById<View>(android.R.id.content).rootView
        rootView.isDrawingCacheEnabled = true
        return rootView.drawingCache
    }

    fun saveBitmap(bitmap: Bitmap) {
        val imagePath = File(Environment.getExternalStorageDirectory().toString() + "/screenshot.png")
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(imagePath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            val uri = Uri.fromFile(imagePath)
            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "image/*"
            val shareBody = "In Tweecher, My highest score with screen shot"
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Tweecher score")
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)

            startActivity(Intent.createChooser(sharingIntent, "Share barber"))

        } catch (e: FileNotFoundException) {
            Log.e("GREC", e.toString())
        } catch (e: IOException) {
            Log.e("GREC", e.toString())
        }

    }



}

class Booking(val name: String, val date: String, val time: String, val address: String)