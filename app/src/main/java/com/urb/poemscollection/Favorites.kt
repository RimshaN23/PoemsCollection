package com.urb.poemscollection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.urb.poemscollection.adapter.Adapter
import com.urb.poemscollection.adapter.FavAdapter
import com.urb.poemscollection.database.DbHelper
import com.urb.poemscollection.model.FavModel
import com.urb.poemscollection.model.PoemsModel

class Favorites : AppCompatActivity() {
    lateinit var arrayList: ArrayList<FavModel>
    lateinit var adapter: FavAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        recyclerView = findViewById(R.id.fav_view)
        recyclerView.layoutManager= LinearLayoutManager(this)
        val home= findViewById<ImageView>(R.id.home)
        home.setOnClickListener {
         finish()
        }


        getFavoriteVideo()

    }

    fun getFavoriteVideo() {
        val db = DbHelper(this)
        arrayList = ArrayList()
        arrayList.addAll(db.viewFav())
        adapter = FavAdapter(this,arrayList)
        recyclerView.adapter = adapter

    }

}