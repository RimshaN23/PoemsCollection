package com.urb.poemscollection

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.search.SearchView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.urb.poemscollection.adapter.Adapter
import com.urb.poemscollection.model.PoemsModel
import java.util.Locale


class MainActivity : AppCompatActivity() {

    lateinit var arrayList: ArrayList<PoemsModel>
    lateinit var adapter: Adapter
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var searchView: androidx.appcompat.widget.SearchView


    lateinit var mAdView : AdView
    private var mInterstitialAd: InterstitialAd? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        searchView= findViewById(R.id.searchview)

        val mainLayout= findViewById<RelativeLayout>(R.id.mainLayout)
        val networkLayout= findViewById<RelativeLayout>(R.id.networkLayout)
        val retry_btn= findViewById<Button>(R.id.retry_btn)

        if (haveNetwork()) {
            //Connected to the internet
            mainLayout.setVisibility(View.VISIBLE);
            networkLayout.setVisibility(View.GONE);
        } else {
            mainLayout.setVisibility(View.GONE);
            networkLayout.setVisibility(View.VISIBLE);
        }
        retry_btn.setOnClickListener {
            if (haveNetwork()) {
                mainLayout.setVisibility(View.VISIBLE)
                networkLayout.setVisibility(View.GONE)
            } else {
                Toast.makeText(this@MainActivity, " Please get Online first. ", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        recyclerView = findViewById<RecyclerView>(R.id.r_view)
        progressBar= findViewById(R.id.progressbar)

        loadData()
        progressBar.visibility= View.GONE


        val myfav= findViewById<ImageView>(R.id.myfav)
        myfav.setOnClickListener {

            showInterAds()
        }

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        loadBanner()
        loadIntersial()

    }

    private fun showInterAds() {

        if (mInterstitialAd!= null){

            mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()

                    val intent= Intent(this@MainActivity,Favorites::class.java)
                    startActivity(intent)
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)


                    val intent= Intent(this@MainActivity,Favorites::class.java)
                    startActivity(intent)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                }

            }

            mInterstitialAd?.show(this)
        }
        else{

            val intent= Intent(this,Favorites::class.java)
            startActivity(intent)
        }

    }

    private fun loadIntersial() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-2929786513595384/9902695813",
            adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }


    private fun loadBanner() {

        Log.e("load_ad", "Loaded")

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
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

    private fun filterList(query: String?) {

        if (query != null){

            val filteredList= ArrayList<PoemsModel>()
            for (i in arrayList){

                if (i.name?.lowercase(Locale.ROOT)?.contains(query)!!){
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()){

            }

            else{
                adapter.updateList(filteredList)
            }
        }

    }

    private fun loadData() {

        Log.e("enter","loaddaata")

        progressBar.visibility= View.VISIBLE

        arrayList=ArrayList()

        val ref= FirebaseDatabase.getInstance().getReference("Poems")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                Log.e("enter","loadref")

                arrayList.clear()
                for (ds in snapshot.children){
                    val model= ds.getValue(PoemsModel::class.java)
                    if (model != null) {
                        arrayList.add(model!!)
                    }

                    val layoutManager = GridLayoutManager(baseContext, 2)

                    adapter= Adapter(this@MainActivity, arrayList, progressBar)
                    recyclerView.adapter= adapter
                    recyclerView.layoutManager= layoutManager

                    if (model != null) {
                        Log.e("enter",model.name.toString())
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



    }

    fun haveNetwork(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals(
                    "WIFI",
                    ignoreCase = true
                )
            ) if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName.equals(
                    "MOBILE",
                    ignoreCase = true
                )
            ) if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }
}