package com.sbiitju.defenseproject.view.ui

import android.app.AlertDialog
import android.app.Application
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbiitju.defenseproject.R

class WorkingViewModel(application: Application):AndroidViewModel(application) {
    val auth=FirebaseAuth.getInstance()


}