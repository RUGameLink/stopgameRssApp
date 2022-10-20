package com.example.stopgamerssapp

import android.app.ProgressDialog
import android.content.AsyncTaskLoader
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stopgamerssapp.Adapter.FeedAdapter
import com.example.stopgamerssapp.Common.HTTPDataHandler
import com.example.stopgamerssapp.Model.RSSObject
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URL
import java.net.URLConnection
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    /*
    result получает нормальный json файл
    Можно подготовить data class под парсинг, спарсить по классике result, напихать в data класс
    Пересобрать ресайклер под дату и на котлине - и все
     */
    private lateinit var toolbar: Toolbar
    private var rssObject: RSSObject? = null

    private val RSS_LINK: String = "https://rss.stopgame.ru/rss_all.xml"
    private val RSS_TO_JSON_API: String = "https://api.rss2json.com/v1/api.json?rss_url="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        toolbar.title = "StopGame НОВОСТИ"
        setActionBar(toolbar)

        setAdapter()
    }

    private fun setAdapter() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        var stringBuilder = StringBuilder(RSS_TO_JSON_API)
        stringBuilder.append(RSS_LINK)
        println("StingBuilder $stringBuilder")
        loadRSS(stringBuilder)
        println("rssTest $rssObject")
    //    recyclerView.adapter = FeedAdapter(rssObject, this)
    }

    private fun loadRSS(link: StringBuilder) {
        try {
            val thread = Thread  {
                var result: String
                var httpDataHandler = HTTPDataHandler()
                result = httpDataHandler.GetHTTPData(link.toString())
                println("resultTest $result")
                rssObject = Gson().fromJson(result, RSSObject::class.java)
            }
            thread.start()
        }
        catch (ex: Exception){
            println(ex)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { //Подключаем меню к action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) { //Слушаем нажатие на кнопку по id
        R.id.menu_refresh -> {
            Toast.makeText(this, "Обновляю данные...", Toast.LENGTH_SHORT).show()
            setAdapter()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
    }
}