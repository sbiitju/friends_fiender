package com.sbiitju.defenseproject.view.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.sbiitju.defenseproject.R
import com.sbiitju.defenseproject.databinding.ActivityAuthintacationBinding
import com.sbiitju.defenseproject.model.data.LatLon
import com.sbiitju.defenseproject.util.ResponseInterface
import com.sbiitju.defenseproject.viewmodel.AuthViewModel

class AuthenticationActivity : AppCompatActivity(),ResponseInterface{
    var authViewModel:AuthViewModel?=null
    var responseInterface:ResponseInterface?=null
    lateinit var auth: FirebaseAuth
    lateinit var progressIndicator: CircularProgressIndicator
    var googleSignInClient:GoogleSignInClient?=null
    var activityAuthenticationBinding:ActivityAuthintacationBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAuthenticationBinding= ActivityAuthintacationBinding.inflate(layoutInflater)
        setContentView(activityAuthenticationBinding?.root)
        supportActionBar?.hide()
        progressIndicator= CircularProgressIndicator(this)
        progressIndicator.show()
        auth= FirebaseAuth.getInstance()
        check(auth.currentUser)
        authViewModel=ViewModelProvider(this)[AuthViewModel::class.java]
        activityAuthenticationBinding?.viewmodel=authViewModel
        authViewModel!!.responseInterface=this
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun check(value: FirebaseUser?) {
        if(value!=null){
            startActivity(Intent(this@AuthenticationActivity,WorkingActivity::class.java))
            finish()
        }
        else {
            progressIndicator.hide()
            return
        }

    }

    override fun onStart() {
        super.onStart()
        auth= FirebaseAuth.getInstance()
    }

    override fun onSuccess(msg: String) {
        TODO("Not yet implemented")
    }

    override fun onUpdate(msg: Any) {
        Log.d("shahin","got it")
        val signInIntent = googleSignInClient!!.signInIntent
        ActivityCompat.startActivityForResult(this@AuthenticationActivity,signInIntent, RC_SIGN_IN,null)
    }

    override fun onFailed(msg: String) {
        TODO("Not yet implemented")
    }

    override fun updateUI(user: FirebaseUser?) {
        TODO("Not yet implemented")
    }

    fun GoogleSignIn(view: View){
        Toast.makeText(this,"Check",Toast.LENGTH_SHORT).show()
        Log.d("SHAHIN","DFAJFHA")

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        progressIndicator.show()

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d("Shahin",user?.displayName.toString())
                    check(user)
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }


}