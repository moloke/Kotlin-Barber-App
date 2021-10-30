package com.example.firstgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class userguide : AppCompatActivity() {
    var mywebview: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userguide)
        mywebview = findViewById<WebView>(R.id.webview)
        mywebview!!.loadUrl("file:///android_asset/Userguide/BarberAppUG.html")
    }
}
