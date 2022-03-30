package com.sbiitju.defenseproject.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel

class TestViewModel(name: String, context: Context) :ViewModel() {
    init {
        Toast.makeText(context, "ViewModelFactory", Toast.LENGTH_SHORT).show()
    }
}