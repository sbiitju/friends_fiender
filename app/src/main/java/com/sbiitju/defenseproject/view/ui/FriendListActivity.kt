package com.sbiitju.defenseproject.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sbiitju.defenseproject.R

class FriendListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)
        var list=intent.getBundleExtra("list")
    }
}