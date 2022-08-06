package com.ads.web.one.quantumdemo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: AdapterNews
    private lateinit var searchView: SearchView
    private lateinit var logoutButton: FloatingActionButton
    private var url = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //link xml components
        recyclerView = findViewById(R.id.recycler_news)
        auth = FirebaseAuth.getInstance()
        searchView = findViewById(R.id.idSV)
        //use shared preference for getting login state(T/F)
        sharedPref =
            getSharedPreferences("Quantum Login", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        logoutButton = findViewById(R.id.logout)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //news api url
        url =
            "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=aeb9eb8f4ca54f20a5717608327e7594"
        //fetching data from url
        fetchData(url)
        logoutButton.setOnClickListener {
            auth.signOut()
            editor.putString("login", "false")
            editor.commit()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                when (query) {
                    "bitcoin" -> {
                        url =
                            "https://newsapi.org/v2/everything?q=${query}&apiKey=aeb9eb8f4ca54f20a5717608327e7594"
                        fetchData(url)
                    }
                    "apple" -> {
                        url =
                            "https://newsapi.org/v2/everything?q=${query}&from=2022-08-05&to=2022-08-05&sortBy=popularity&apiKey=aeb9eb8f4ca54f20a5717608327e7594"
                        fetchData(url)

                    }
                    "trump" -> {
                        url =
                            "https://newsapi.org/v2/top-headlines?q=${query}&apiKey=aeb9eb8f4ca54f20a5717608327e7594"
                        fetchData(url)
                    }
                    "us" -> {
                        url =
                            "https://newsapi.org/v2/top-headlines?country=${query}&apiKey=aeb9eb8f4ca54f20a5717608327e7594"
                        fetchData(url)
                    }
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query == "") {
                    url =
                        "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=aeb9eb8f4ca54f20a5717608327e7594"
                    fetchData(url)
                } else {
                    url =
                        "https://newsapi.org/v2/everything?q=${query}&apiKey=aeb9eb8f4ca54f20a5717608327e7594"
                    fetchData(url)
                }
                return false
            }

        })
        newsAdapter = AdapterNews()
        recyclerView.adapter = newsAdapter
    }

    private fun fetchData(url: String) {
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                //response listener and fetch the required data
                val newsJsonArray = response.getJSONArray("articles")
                val newsArray = ArrayList<ModalNews>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val title = newsJsonObject.getString("title")
                    val author = newsJsonObject.getString("author")
                    val publishedAt = newsJsonObject.getString("publishedAt")
                    val description = newsJsonObject.getString("description")
                    val urlImg = newsJsonObject.getString("urlToImage")

                    //convert published at into date and time
                    val year = publishedAt.substring(0, 4)
                    val month = publishedAt.substring(5, 7)
                    val date = publishedAt.substring(8, 10)
                    val hour = publishedAt.substring(11, 13)
                    val min = publishedAt.substring(14, 16)
                    val updatedDate = "$date-$month-$year  "
                    var print = ""
                    var convertHour = hour.toInt()
                    val convertMin = min.toInt()
                    if (convertHour == 12) {
                        convertHour = 12
                        print = "PM"
                    } else if (convertHour > 11 && convertMin > 0) {
                        convertHour -= 12
                        print = "PM"
                    } else {
                        print = "AM"
                    }
                    val newHour = convertHour.toString()
                    val updatedTime = "$updatedDate$newHour:$min $print"

                    val news = ModalNews(
                        title, author, updatedTime, description, urlImg
                    )
                    newsArray.add(news)
                }
//updating the recyclerview
                newsAdapter.updateData(newsArray)
            },
            { _ ->

            }
        ) {
            //writing authorization headers
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}