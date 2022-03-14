package com.sbiitju.defenseproject.util

import com.google.firebase.auth.FirebaseUser

interface ResponseInterface {
    fun onSuccess(msg:String)
    fun onUpdate(msg:Any)
    fun onFailed(msg:String)
    abstract fun updateUI(user: FirebaseUser?)
}