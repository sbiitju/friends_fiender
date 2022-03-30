package com.sbiitju.defenseproject.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sbiitju.defenseproject.FindLocation
import com.sbiitju.defenseproject.R
import com.sbiitju.defenseproject.viewmodel.AuthViewModel
import com.sbiitju.defenseproject.viewmodel.MainViewModelFactory
import com.sbiitju.defenseproject.viewmodel.TestViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this,AuthenticationActivity::class.java))
        finish()
        var viewModelFactory=MainViewModelFactory("Shahin Bashar",this.applicationContext)
        var viewModel=ViewModelProvider(this,viewModelFactory)[TestViewModel::class.java]
//        val model=TestViewModel("St",this.applicationContext)
        var authViewModel=ViewModelProvider(this)[AuthViewModel::class.java]
    }
}