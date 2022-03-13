package com.sbiitju.defenseproject.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sbiitju.defenseproject.util.ResponseInterface

class AuthViewModel: ViewModel() {
    var responseInterface:ResponseInterface?=null
    fun signIn(view: View){
        Log.d("shahin","shahin")
        var gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
        gso.requestEmail().build()
        responseInterface?.onUpdate(gso)

    }
}