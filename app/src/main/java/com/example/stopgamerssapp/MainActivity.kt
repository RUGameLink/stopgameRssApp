package com.example.stopgamerssapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun getInputStream(url: URL): InputStream? {
        try {
            return url.openConnection().getInputStream()
        }
        catch (ex: Exception){
            println(ex)
            return null
        }
    }

    private class ProcessInBackground external Asyn
}