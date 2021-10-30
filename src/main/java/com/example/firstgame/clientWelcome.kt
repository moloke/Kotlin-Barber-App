package com.example.firstgame

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_barber_login.*
import kotlinx.android.synthetic.main.activity_client_welcome.*
import java.util.*

private const val PERMISSION_REQUEST = 10
class clientWelcome : AppCompatActivity() {

    lateinit var locationManager: LocationManager
    private var hasGps = false

    private var locationGps: Location? = null


    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_welcome)
        supportActionBar?.title =  "Client"




        location_button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkPermission(permissions)) {
                    enableView()
                } else {
                    requestPermissions(permissions, PERMISSION_REQUEST)
                }
            } else {
                enableView()
            }
        }

        clientSearchBtn.setOnClickListener {
            if (postCodetxt.text.isEmpty()){
                postCodetxt.error = "Postcode required"
        }else {
            val intent = Intent(this@clientWelcome, clientSearch::class.java)
            intent.putExtra("Postcode", postCodetxt.text.toString())
            startActivity(intent)
        }


        }


    }

    private fun disableView() {
        location_button.isEnabled = false
        location_button.alpha = 0.5F
    }

    private fun enableView() {
        location_button.isEnabled = true
        location_button.alpha = 1F

            getLocation()


            val myCoordinates = LatLng(locationGps!!.latitude, locationGps!!.longitude)
            val myPosteCode : String
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(myCoordinates.Lat, myCoordinates.Long, 1)
            val address = addresses.get(0).getAddressLine(0)
            myPosteCode = addresses.get(0).postalCode

            postCodetxt.setText(myPosteCode)




    }



    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)


        if (hasGps) {
            Log.d("CodeAndroidLocation", "hasGps")
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object :
                LocationListener {

                override fun onLocationChanged(location: Location?) {
                    if (location != null) {
                        locationGps = location
                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                }

                override fun onProviderEnabled(provider: String?) {

                }

                override fun onProviderDisabled(provider: String?) {

                }

            })

            val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (localGpsLocation != null)
                locationGps = localGpsLocation
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }

        if(locationGps == null){
            Toast.makeText(this, "Location cannot be found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess)
                enableView()

        }
    }

    class LatLng (val Lat: Double, val Long: Double)
}