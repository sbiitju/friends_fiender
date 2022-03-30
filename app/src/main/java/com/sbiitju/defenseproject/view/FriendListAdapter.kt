package com.sbiitju.defenseproject.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sbiitju.defenseproject.R
import com.sbiitju.defenseproject.model.data.Profile
import com.sbiitju.defenseproject.view.ui.MapsActivity

class FriendListAdapter(var context: Context,var friendList:List<Profile>):
    RecyclerView.Adapter<FriendViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        var view= LayoutInflater.from(context).inflate(R.layout.friendscard,null)
        return FriendViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bloodTxt.setText(friendList[position].blood)
        holder.emailTxt.setText(friendList[position].email)
        holder.nameTxt.setText(friendList[position].name)
        Glide.with(context).load(friendList[position].photoUrl).into(holder.profilePhoto)
        holder.callBtn.setOnClickListener {
            dialContactPhone(friendList[position].phone.toString())
        }
        holder.getBtn.setOnClickListener {
            var intent=Intent(context,MapsActivity::class.java)
            intent.putExtra("lat",friendList[position].uid?.latLon?.lat)
            intent.putExtra("name",friendList[position].name)
            intent.putExtra("lon",friendList[position].uid?.latLon?.lon)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return friendList.size
    }
    private fun dialContactPhone(number1: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$number1")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        context.startActivity(callIntent)
    }
}
class FriendViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var profilePhoto=itemView.findViewById<ImageView>(R.id.friendsPhoto)
    var callBtn=itemView.findViewById<Button>(R.id.callFriends)
    var getBtn=itemView.findViewById<Button>(R.id.getfriends)
    var nameTxt=itemView.findViewById<TextView>(R.id.cardName)
    var emailTxt=itemView.findViewById<TextView>(R.id.cardEmail)
    var bloodTxt=itemView.findViewById<TextView>(R.id.cardBlood)
}


