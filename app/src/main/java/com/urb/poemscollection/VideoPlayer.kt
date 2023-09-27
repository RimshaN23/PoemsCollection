package com.urb.poemscollection

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class VideoPlayer : AppCompatActivity() {

    private lateinit var videoView: VideoView
    var isfullscreen= true

    lateinit var progressBar: ProgressBar
    lateinit var layout: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE


        videoView = findViewById(R.id.videoView)
        progressBar= findViewById(R.id.progressbar1)

        val fullscreen = findViewById<ImageView>(R.id.fullscreen)
        val back = findViewById<ImageView>(R.id.back)
         layout= findViewById<RelativeLayout>(R.id.layout)

////////////////        showButtonAfterDelay()

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
//        videoView.setMediaController(object : MediaController(this) {
//            override fun dispatchKeyEvent(event: KeyEvent): Boolean {
//                if (event.keyCode == KeyEvent.KEYCODE_BACK) (context as Activity).finish()
//                return super.dispatchKeyEvent(event)
//            }
//        })

        back.setOnClickListener {
            finish()
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
    private fun showButtonAfterDelay() {
        // Delay for 3 seconds (adjust as needed)
        val delayMillis = 3000L

        layout.postDelayed({
            layout.visibility = View.VISIBLE
            hideButtonAfterDelay()
        }, delayMillis)
    }

    private fun hideButtonAfterDelay() {
        // Delay for 3 seconds (adjust as needed)
        val delayMillis = 3000L

        layout.postDelayed({
            layout.visibility = View.INVISIBLE // or View.GONE if you prefer
        }, delayMillis)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // When the screen is touched, make the button visible again
        layout.visibility = View.VISIBLE
        hideButtonAfterDelay()
        return super.onTouchEvent(event)
    }
}

