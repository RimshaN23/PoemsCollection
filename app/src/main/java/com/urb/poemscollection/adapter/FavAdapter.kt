package com.urb.poemscollection.adapter

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.squareup.picasso.Picasso
import com.urb.poemscollection.R
import com.urb.poemscollection.VideoPlayer
import com.urb.poemscollection.database.DbHelper
import com.urb.poemscollection.model.FavModel

class FavAdapter(var context: Context, var arrayList: ArrayList<FavModel>):RecyclerView.Adapter<FavAdapter.ViewHolder>()   {
    private var mInterstitialAd: InterstitialAd? = null
    var uri: String?= null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemview,parent,false)
        return FavAdapter.ViewHolder(itemView)
    }

    override fun getItemCount() = arrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item= arrayList[position]
        val id: Int? =item.id
        val name: String? =item.name
        val thumbnail: String? =item.thumbnail
        val videoUri: String? =item.videoUri

        val imgUri: Uri = Uri.parse(thumbnail)
        uri= videoUri
        holder.name.text= name
        Picasso.get().load(imgUri).into(holder.thumbnail)

        loadIntersial()

        holder.card.setOnClickListener {
//            val intent= Intent(context, VideoPlayer::class.java)
//            intent.putExtra("videoUri", videoUri)
//            context.startActivity(intent)

            showInterAds()
        }

        holder.fav.setOnClickListener {

            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Remove Video")
            dialog.setMessage("Do you want to delete this video from favorites?")
            dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                deleteItem(position)

            })

            dialog.show()
            val db = DbHelper(context)
            db.removeFav(id)
        }

    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        var name: TextView = itemView.findViewById(R.id.title)
        var card: CardView = itemView.findViewById(R.id.cardview)
        var fav: ImageView = itemView.findViewById(R.id.remove_favorite)
    }
    fun deleteItem(position: Int) {
        arrayList.removeAt(position)
        notifyItemRemoved(position)
        Log.e("delit", "del")

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

}