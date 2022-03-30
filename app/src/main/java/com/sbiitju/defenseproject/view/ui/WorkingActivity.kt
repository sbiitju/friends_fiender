package com.sbiitju.defenseproject.view.ui

import android.Manifest
import android.app.AlertDialog
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.sbiitju.defenseproject.FindLocation
import com.sbiitju.defenseproject.MyService
import com.sbiitju.defenseproject.R
import com.sbiitju.defenseproject.databinding.ActivityWorkingBinding
import com.sbiitju.defenseproject.model.data.LatLon
import com.sbiitju.defenseproject.model.data.Profile
import com.sbiitju.defenseproject.model.data.UID
import com.sbiitju.defenseproject.view.FriendListAdapter
import com.sbiitju.defenseproject.view.RequestListAdapter


class WorkingActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    var myProfile:Profile?=null
//    private lateinit var locationListener: LocationListener
//    private lateinit var locationManager: LocationManager
    lateinit var workingViewModel: WorkingViewModel
    private lateinit var binding: ActivityWorkingBinding
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private val firebaseAuth: FirebaseAuth? = null
    private var latLon: LatLon? = null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getlocation()
        startService(Intent(this,MyService::class.java))
//        getGeoLocation()
        auth= FirebaseAuth.getInstance()
        binding = ActivityWorkingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        workingViewModel=ViewModelProvider(this).get(WorkingViewModel::class.java)
        check()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        binding.apply {
//            Glide.with(this@WorkingActivity).load(auth.currentUser?.photoUrl).circleCrop().into(topbarImage)
//            displayName.text=auth.currentUser?.displayName
//
//        }
        binding.nearestPolice.setOnClickListener {
            Toast.makeText(this, "touched", Toast.LENGTH_SHORT).show()
            searchWeb("Police Station")
        }
        binding.myFriendList.setOnClickListener {
            var profile:Profile?=null
           val ref=FirebaseDatabase.getInstance().getReference(auth.uid.toString())
            ref.child("profile").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var list= mutableListOf<Profile>()
                      var freindRf=FirebaseDatabase.getInstance().getReference(auth.uid!!)
                        freindRf.child("freiendList").addValueEventListener(object :ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(s in snapshot.children){
                                    var p=s.getValue<Profile>()
                                    if (p != null) {
                                        list.add(p)
                                    }
                                }
                                Toast.makeText(this@WorkingActivity, "test", Toast.LENGTH_SHORT).show()
                                var builder=AlertDialog.Builder(this@WorkingActivity)
                                var view=LayoutInflater.from(this@WorkingActivity).inflate(R.layout.friendlist,null)
                                var recyclerView=view.findViewById<RecyclerView>(R.id.recyclerView)
                                var adapter=FriendListAdapter(this@WorkingActivity,list)
                                recyclerView.layoutManager=LinearLayoutManager(this@WorkingActivity,LinearLayoutManager.VERTICAL,false)
                                recyclerView.adapter=adapter
                                builder.setView(view).show()
                                builder.setTitle("My Friend List")
                                Log.d("shahin",list.toString())

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })

                    }
                    else{
                        Toast.makeText(this@WorkingActivity, "Doesn't found", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
        binding.traceMe.setOnClickListener {
            var ref=FirebaseDatabase.getInstance().getReference(auth.uid.toString())
            ref.child("request").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var list= mutableListOf<Profile>()
                    if (snapshot.exists()){
                       snapshot.children.forEach{
                           it.getValue<Profile>()?.let { it1 -> list.add(it1) }
                       }
                        var builder=AlertDialog.Builder(this@WorkingActivity)
                        var view=LayoutInflater.from(this@WorkingActivity).inflate(R.layout.friendlist,null)
                        var recyclerView=view.findViewById<RecyclerView>(R.id.recyclerView)
                        var adapter=RequestListAdapter(this@WorkingActivity,list)
                        Toast.makeText(this@WorkingActivity, list.toString(), Toast.LENGTH_SHORT).show()
                        recyclerView.layoutManager=LinearLayoutManager(this@WorkingActivity,LinearLayoutManager.VERTICAL,false)
                        recyclerView.adapter=adapter
                        builder.setView(view).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
        binding.nearestFriend.setOnClickListener {
            var locationList= mutableListOf<Profile>()
            FirebaseDatabase.getInstance().getReference(auth.uid.toString()).child("freiendList").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        snapshot.children.forEach{
                            it.getValue<Profile>()?.let { it1 -> locationList.add(it1) }
                        }
                        locationList.forEach {
                            var value1=myProfile?.uid?.latLon
                            var value2=it.uid?.latLon
                            var location=FindLocation.distance(value1?.lat!!.toDouble(),value2?.lat!!.toDouble(),value1?.lon!!.toDouble(),value2?.lon!!.toDouble())
                            if (location<5){
                                dialContactPhone(it.phone.toString())
                            }
                            Log.d("Shahin",location.toString())
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
        binding.nearestHospital.setOnClickListener {
            searchWeb("Hospital")

        }
        binding.nearestATM.setOnClickListener {
            searchWeb("ATM Booth")
        }
        binding.call999.setOnClickListener {
            dialContactPhone("999")
        }
        binding.searchBtn.setOnClickListener {
            var searchKey=binding.searchET.text.toString()
            FirebaseDatabase.getInstance().getReference(searchKey).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var profile=snapshot.getValue<Profile>()
                        var dialog=AlertDialog.Builder(this@WorkingActivity)
                        var view=LayoutInflater.from(this@WorkingActivity).inflate(R.layout.searchresult,null)
                        var image=view.findViewById<ImageView>(R.id.searchPhoto);
                        Glide.with(this@WorkingActivity).load(profile?.photoUrl).circleCrop().into(image)
                        var nameView=view.findViewById<TextView>(R.id.searcName)
                        nameView.setText(profile?.name)
                        var email=view.findViewById<TextView>(R.id.searchEmail)
                        email.setText(profile?.email)
                        val requestBtn=view.findViewById<Button>(R.id.requestBtn)
                            requestBtn.setOnClickListener {
                                val reference=FirebaseDatabase.getInstance().getReference(profile?.uid?.uid.toString())
                                reference.child("request").child(myProfile?.phone.toString()).setValue(myProfile).addOnCompleteListener {
                                    if(it.isSuccessful){
                                        requestBtn.visibility=View.GONE
                                        Toast.makeText(this@WorkingActivity, "Request Sent to ${profile?.name}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                        dialog.setView(view).show()
                    }else{
                        Toast.makeText(this@WorkingActivity, "Nei", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

    }
    private fun dialContactPhone(number1: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$number1")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        startActivity(callIntent)
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun getlocation() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val lat = location.latitude.toString()
                val lon = location.longitude.toString()
                latLon = LatLon(lat, lon)
                val uid: String = auth.getCurrentUser()!!.getUid()
                latLon?.let {
                    shareLatLon(it) }
                val databaseReference = FirebaseDatabase.getInstance().getReference(uid)
                databaseReference.child("Profile")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {}
                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 10
            )
            return
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                config()
            }
        }
    }

    private fun shareLatLon(latLon: LatLon) {
        FirebaseDatabase.getInstance().getReference(auth.uid!!).child("profile").child("uid").setValue(UID(auth.uid,latLon))
        val reference=FirebaseDatabase.getInstance().getReference(auth?.currentUser?.email?.subSequence(0,13)
            .toString())
        reference.setValue(latLon)
        var uid=UID(auth.uid,latLon)
        val newReference=FirebaseDatabase.getInstance().getReference("location")
        auth.uid?.let { newReference.child(it) }?.setValue(uid)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun config() {
        if (ActivityCompat.checkSelfPermission(
                this@WorkingActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@WorkingActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            return
        }
        locationListener?.let {
            locationManager?.requestLocationUpdates(
                "gps",
                10000, 0f, it
            )
        }
    }

    private  fun makeProfile() {
        var profile:Profile?=null
        val auth=FirebaseAuth.getInstance()
        var builder= AlertDialog.Builder(this)
        val view1= LayoutInflater.from(this).inflate(R.layout.profile,null)
        var nameText=view1.findViewById<TextView>(R.id.nameDisply)
        nameText.text=auth.currentUser?.displayName
        var emailText=view1.findViewById<TextView>(R.id.profileEmail)
        var bloodSelection=view1.findViewById<Spinner>(R.id.profileBlood)
        var blood=bloodSelection.selectedItem.toString()
        emailText.text=auth.currentUser?.email
        var profilePhote=view1.findViewById<ImageView>(R.id.profileImage)
        Glide.with(this).load(auth.currentUser?.photoUrl).circleCrop().into(profilePhote)
        var numberET=view1.findViewById<TextInputEditText>(R.id.profileNumber)
        var submitBtn=view1.findViewById<Button>(R.id.updateProfile)
        submitBtn.setOnClickListener {
            val number=numberET.text.toString()
            var uid=UID(auth.uid,latLon)
            profile=Profile(auth.currentUser?.displayName,auth.currentUser?.email,number,auth.currentUser?.photoUrl.toString(),blood,uid)
            val reference=FirebaseDatabase.getInstance().getReference(number)
            reference.setValue(profile)
            val numberRef=FirebaseDatabase.getInstance().getReference(auth.uid!!)
            numberRef.child("profile").setValue(profile).addOnCompleteListener {
                if(it.isSuccessful){
                    startActivity(Intent(this,WorkingActivity::class.java))
                }else{
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
//            numberRef.child("location").setValue()
        }
        builder.setCancelable(false)
//        return profile

        builder.setView(view1).show()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.common, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logOut -> {
                FirebaseAuth.getInstance().signOut()
                stopService(Intent(this,MyService::class.java))
                startActivity(Intent(this, AuthenticationActivity::class.java))
                finish()
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun check(){
        val database= auth.currentUser?.let { FirebaseDatabase.getInstance().getReference(it.uid) }
        if (database != null) {
            database.child("profile").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        myProfile=snapshot.getValue<Profile>()
                        Toast.makeText(this@WorkingActivity, "Your profile is up-to-date", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@WorkingActivity, "Make Your profile first", Toast.LENGTH_SHORT).show()
                        makeProfile()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }
    fun searchWeb(query: String?) {
        val intent = Intent(Intent.ACTION_WEB_SEARCH)
        intent.putExtra(SearchManager.QUERY, query)
        startActivity(intent)
    }
}