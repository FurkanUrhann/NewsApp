package com.works

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var newsList: List<News>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        val result = Result()

        val run = Runnable {
            newsList = result.news()
            runOnUiThread {
                val adapter = Adapter(newsList)
                listView.adapter = adapter

                listView.setOnItemClickListener { _, _, position, _ ->
                    val intent = Intent(this@MainActivity, WebViewActivity::class.java)
                    intent.putExtra("url", newsList[position].href)
                    startActivity(intent)
                }
            }
        }
        Thread(run).start()
    }

    inner class Adapter(private val newsList: List<News>) : BaseAdapter() {
        override fun getCount(): Int {
            return newsList.size
        }

        override fun getItem(position: Int): Any {
            return newsList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(this@MainActivity).inflate(R.layout.list_item, parent, false)

            val imageView = view.findViewById<ImageView>(R.id.imageView)
            val textView = view.findViewById<TextView>(R.id.textView)

            val news = newsList[position]

            textView.text = news.title
            Glide.with(this@MainActivity).load(news.img).into(imageView)

            return view
        }
    }
}

