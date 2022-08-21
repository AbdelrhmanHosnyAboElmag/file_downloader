package com.udacity

import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {
    private lateinit var state_tv:TextView
    private lateinit var download_tv:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        download_tv=findViewById(R.id.tv_download)
        state_tv=findViewById(R.id.tv_fail_suscces)
        val intent = intent
        val url = intent.getStringExtra("DOWNLOAD_FILE")
        val state = intent.getStringExtra("STATUS")
        if(state.equals("SUCCESS",true)){
            state_tv.setTextColor(Color.parseColor("#00FF00"))
        }else{
            state_tv.setTextColor(Color.parseColor("#ff0000"))
        }
        state_tv.text=state
        download_tv.text=url
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelNotifications()
    }
}
