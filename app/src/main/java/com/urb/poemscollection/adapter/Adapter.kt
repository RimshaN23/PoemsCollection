package com.urb.poemscollection.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.squareup.picasso.Picasso
import com.urb.poemscollection.Favorites
import com.urb.poemscollection.R
import com.urb.poemscollection.VideoPlayer
import com.urb.poemscollection.database.DbHelper
import com.urb.poemscollection.model.PoemsModel

class Adapter( var context: Context, var model: ArrayList<PoemsModel>, var progressBar: ProgressBar):RecyclerView.Adapter<Adapter.ViewHolder>()  {

    private var mInterstitialAd: InterstitialAd? = null
    var uri: String?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemview_grid,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount()= model.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= model!![position]
        val id: String? =item.id
        val name: String? =item.name
        val thumbnail: String? =item.thumbnail
        var videoUri: String? =item.videoUri

        uri= videoUri

        val imgUri: Uri= Uri.parse(thumbnail)

        holder.name.text= name
        Picasso.get().load(imgUri).into(holder.thumbnail)

        loadIntersial()

        holder.card.setOnClickListener {
            showInterAds()
//            val intent=Intent(context,VideoPlayer::class.java)
//            intent.putExtra("videoUri", videoUri)
//            context.startActivity(intent)
        }

        holder.fav.setOnClickListener {
            val db = DbHelper(context)

            if (!name?.let { it1 -> db.isNameExists(it1) }!!) {
                // Name does not exist, add the new record
                if (thumbnail != null && videoUri != null) {
                    db.addFav(
                        name,thumbnail,videoUri
                    )

                    Toast.makeText(context, "Added to Favorites successfully", Toast.LENGTH_SHORT).show();

                }
            } else {
                // Name already exists
                Toast.makeText(context, "Video already exists in Favorites", Toast.LENGTH_SHORT).show();
            }


        }

    }
    fun onDataChanged() {
        progressBar.visibility= View.GONE
    }

    fun updateList(newList: ArrayList<PoemsModel>){
        this.model= newList


        notifyDataSetChanged()
    }
    private fun showInterAds() {

        if (mInterstitialAd!= null){

            mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    val intent=Intent(context,VideoPlayer::class.java)
                    intent.putExtra("videoUri", uri)
                    context.startActivity(intent)

                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    val intent=Intent(context,VideoPlayer::class.java)
                    intent.putExtra("videoUri", uri)
                    context.startActivity(intent)

                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                }

            }

            mInterstitialAd?.show(context as Activity)
        }
        else{
            val intent=Intent(context,VideoPlayer::class.java)
            intent.putExtra("videoUri", uri)
            context.startActivity(intent)

        }

    }

    private fun loadIntersial() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(context,"ca-app-pub-2929786513595384/9902695813",
            adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        var name: TextView = itemView.findViewById(R.id.title)
        var card: CardView = itemView.findViewById(R.id.cardview)
        var fav: ImageView= itemView.findViewById(R.id.add_fav)
    }

}