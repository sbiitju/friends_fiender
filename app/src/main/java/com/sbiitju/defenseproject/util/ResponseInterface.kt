package com.sbiitju.defenseproject.util

interface ResponseInterface {
    fun onSuccess(msg:String)
    fun onUpdate(msg:Any)
    fun onFailed(msg:String)
}