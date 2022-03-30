package com.sbiitju.defenseproject.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sbiitju.defenseproject.R
import com.sbiitju.defenseproject.model.data.Profile

class RequestListAdapter(var context: Context,var requestList:List<Profile>) :RecyclerView.Adapter<RequestViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.acceptcard,null)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        var auth:FirebaseAuth= FirebaseAuth.getInstance()
        holder.emailTxt.setText(requestList[position].email)
        holder.nameTxt.setText(requestList[position].name)
        Glide.with(context).load(requestList[position].photoUrl).into(holder.profilePhoto)
        holder.acceptBtn.setOnClickListener {
            FirebaseDatabase.getInstance().getReference(auth.uid.toString()).child("freiendList").child(requestList[position]?.phone.toString()).setValue(requestList[position])
            FirebaseDatabase.getInstance().getReference(auth.uid.toString()).child("request").child(requestList[position]?.phone.toString()).removeValue()
        }

    }

    override fun getItemCount(): Int {
        return requestList.size
    }
}
class RequestViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var profilePhoto=itemView.findViewById<ImageView>(R.id.searchPhoto)
    var acceptBtn=itemView.findViewById<Button>(R.id.requestBtn)
    var nameTxt=itemView.findViewById<TextView>(R.id.searcName)
    var emailTxt=itemView.findViewById<TextView>(R.id.searchEmail)
}