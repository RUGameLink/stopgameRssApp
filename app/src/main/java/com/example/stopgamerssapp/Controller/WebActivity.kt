package com.example.stopgamerssapp.Controller

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stopgamerssapp.R

class WebActivity: AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        init()
        val request = intent.getStringExtra( "linkData" ).toString()
        loadPage(this, request)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPage(context: Context, request: String) {
        if (context.isConnectedToNetwork()) {
            webView.webViewClient = WebViewClient()
            webView.webChromeClient = WebChromeClient()
            // включаем поддержку JavaScript
            webView.settings.javaScriptEnabled = true
            // указываем страницу загрузки
            //    webView.loadUrl("https://www.istu.edu/schedule/")
            webView.loadUrl(request)


            webView.webViewClient = object : WebViewClient() {

                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {

                    super.onPageFinished(view, url)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun Context.isConnectedToNetwork(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }

    private fun init(){
        webView = findViewById(R.id.webView)
    }
}