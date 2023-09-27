package com.urb.poemscollection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.urb.poemscollection.adapter.Adapter
import com.urb.poemscollection.adapter.FavAdapter
import com.urb.poemscollection.database.DbHelper
import com.urb.poemscollection.model.FavModel
import com.urb.poemscollection.model.PoemsModel

class Favorites : AppCompatActivity() {
    lateinit var arrayList: ArrayList<FavModel>
    lateinit var adapter: FavAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var mAdView : AdView


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
        loadBanner()

    }

    fun getFavoriteVideo() {
        val db = DbHelper(this)
        arrayList = ArrayList()
        arrayList.addAll(db.viewFav())
        adapter = FavAdapter(this,arrayList)
        recyclerView.adapter = adapter

    }

    private fun loadBanner() {

        Log.e("load_ad", "Loaded")

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView2)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //     Toast.makeText(this@MainActivity,"Ad Load", Toast.LENGTH_SHORT).show()

            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }
    }


}