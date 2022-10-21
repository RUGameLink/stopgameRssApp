package com.example.stopgamerssapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stopgamerssapp.Adapter.FeedAdapter
import com.example.stopgamerssapp.Common.HTTPDataHandler
import com.example.stopgamerssapp.Interface.ItemClickListener
import com.example.stopgamerssapp.Model.StopGameNews
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), ItemClickListener {

    private val newsList = ArrayList<StopGameNews>()

    private val RSS_LINK: String = "https://rss.stopgame.ru/rss_all.xml"
    private val RSS_TO_JSON_API: String = "https://api.rss2json.com/v1/api.json?rss_url="

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var stringBuilder = StringBuilder(RSS_TO_JSON_API)
        stringBuilder.append(RSS_LINK)
        println("StingBuilder $stringBuilder")

        if(isOnline(this)){
            loadRSS(stringBuilder)
        }
        else{
            Toast.makeText(this, "Отсутствует подключение к сети", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setAdapter(newsList: ArrayList<StopGameNews>) {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = FeedAdapter(newsList, this, this)
    }

    private fun loadRSS(link: StringBuilder) {
        try {
            val thread = Thread  {
                var result: String
                var httpDataHandler = HTTPDataHandler()
                result = httpDataHandler.GetHTTPData(link.toString())
                val jsonObject = JSONTokener(result).nextValue() as JSONObject
                var error = jsonObject.optString("status")
                val jsonArray = jsonObject.getJSONArray("items")
                if(error.equals("ok")){
                    println("resultTest $result")
                    println("jsonArray $jsonArray")
                    runOnUiThread {
                        for (i in 0 until jsonArray.length()) {
                            val title = jsonArray.getJSONObject(i).getString("title")
                            val pubDate = jsonArray.getJSONObject(i).getString("pubDate")
                            val link = jsonArray.getJSONObject(i).getString("link")
                            val content = jsonArray.getJSONObject(i).getString("content")
                            val enclosure = jsonArray.getJSONObject(i).getJSONObject("enclosure").getString("link")

                            println("link: $enclosure")

                            newsList.add(StopGameNews(title, content, pubDate, enclosure, link))
                        }
                        setAdapter(newsList)
                    }
                }
                else{
                    runOnUiThread {
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    }
                }

            //    rssObject = Gson().fromJson(result, RSSObject::class.java)
            }
            thread.start()
        }
        catch (ex: Exception){
            println(ex)
        }


    }

    override fun itemOnClick(position: Int) {
        val url = newsList[position].link
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun itemOnLongClick(position: Int) {
        val intent= Intent()
        intent.action=Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT,"${newsList[position].link}")
        intent.type="text/plain"
        startActivity(Intent.createChooser(intent,"Share To:"))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { //Подключаем меню к action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) { //Слушаем нажатие на кнопку по id
        R.id.menu_refresh -> {
            Toast.makeText(this, "Обновляю данные...", Toast.LENGTH_SHORT).show()
            var stringBuilder = StringBuilder(RSS_TO_JSON_API)
            stringBuilder.append(RSS_LINK)
            println("StingBuilder $stringBuilder")

            if(isOnline(this)){
                loadRSS(stringBuilder)
            }
            else{
                Toast.makeText(this, "Отсутствует подключение к сети", Toast.LENGTH_SHORT).show()
            }
            true
        }
        R.id.menu_logo -> {
            val url = "https://stopgame.ru/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}