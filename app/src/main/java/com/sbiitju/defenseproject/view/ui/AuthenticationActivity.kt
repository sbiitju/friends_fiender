package com.sbiitju.defenseproject.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sbiitju.defenseproject.R
import com.sbiitju.defenseproject.databinding.ActivityAuthenticationBinding
import com.sbiitju.defenseproject.util.ResponseInterface
import com.sbiitju.defenseproject.viewmodel.AuthViewModel

class AuthenticationActivity : AppCompatActivity(),ResponseInterface{
    var authViewModel:AuthViewModel?=null
    var activityAuthenticationBinding:ActivityAuthenticationBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        activityAuthenticationBinding= ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(activityAuthenticationBinding?.root)
        authViewModel=ViewModelProvider(this)[AuthViewModel::class.java]
        activityAuthenticationBinding?.viewModel=authViewModel
    }

    override fun onSuccess(msg: String) {
        TODO("Not yet implemented")
    }

    override fun onUpdate(msg: Any) {
        var info=GoogleSignIn.getClient(this, msg as GoogleSignInOptions)
        Log.d("shahin",info.toString())
    }

    override fun onFailed(msg: String) {
        TODO("Not yet implemented")
    }
}