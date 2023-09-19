package com.urb.poemscollection

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class VideoPlayer : AppCompatActivity() {

    private lateinit var videoView: VideoView
    var isfullscreen= false

    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        videoView = findViewById(R.id.videoView)
        progressBar= findViewById(R.id.progressbar1)

        val fullscreen = findViewById<ImageView>(R.id.fullscreen)
   //     val videoUri = Uri.parse("https://www.dailymotion.com/video/x53wb20") // Replace with your video URI


        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        val uri = intent.extras!!.getString("videoUri")


      //  "android.resource://$packageName" + "/" + R.raw.babyshark
        val videoUri = Uri.parse(uri) // Replace with your video URI


        videoView.setVideoURI(videoUri)
        videoView.start()
        videoView.setOnPreparedListener {
            progressBar.visibility= View.GONE
        }



        fullscreen.setOnClickListener {
            if (!isfullscreen)
            {
                fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.baseline_fullscreen_exit_24))
                requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
            else{
                fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.baseline_fullscreen_24))
                requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            isfullscreen= !isfullscreen
        }



    }
}