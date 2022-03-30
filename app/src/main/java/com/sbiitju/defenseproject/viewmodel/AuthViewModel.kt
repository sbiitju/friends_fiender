package com.sbiitju.defenseproject.viewmodel

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sbiitju.defenseproject.R
import com.sbiitju.defenseproject.util.ResponseInterface

class AuthViewModel: ViewModel() {
    var responseInterface:ResponseInterface?=null
    fun signIn(view: View){
        Log.d("shahin","shahin")
        // Configure Google Sign In
        val googleSignInClient:GoogleSignInClient?=null
        responseInterface?.onUpdate("check")
    }
    val test:String?=null
    fun onSkip(){

    }
}