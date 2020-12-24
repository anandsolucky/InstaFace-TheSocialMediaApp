package com.example.socialmediaapp.startup

import android.content.Intent
import android.net.sip.SipErrorCode.TIME_OUT
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaapp.R
import kotlinx.android.synthetic.main.activity_sign_up_info.*
import java.util.*
import java.util.concurrent.TimeUnit


class SignUpInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_info)
        Handler().postDelayed(Runnable {
            faddingText.stop()
            nextImage.visibility = View.VISIBLE
        }, 14000)

        nextImage.setOnClickListener {
            startActivity(Intent(this, SignUpBasicInfoActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val infos = arrayOf(
            "Hi",
            "Thank you for registering",
            "one last step",
            "Please share few basic details about you"
        )
        faddingText.setTexts(infos)
        faddingText.setTimeout(3500, TimeUnit.MILLISECONDS)
        faddingText.forceRefresh()
    }
}