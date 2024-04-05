package com.example.secure

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.example.secure.databinding.ActivityMainBinding
import com.example.secure.databinding.ActivitySigInBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.fragment.app.activityViewModels


class SigInActivity : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    private val viewModel: DataViewModel by viewModels { DrugItemModelFactory((application as SecureApplication).repository)}
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivitySigInBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val i = Intent(this, MainActivity::class.java)
//        i.putExtra("username", auth.currentUser!!.displayName.toString())
//        i.putExtra("email", auth.currentUser?.email.toString())
//        i.putExtra("number",auth.currentUser?.phoneNumber.toString())
//        i.putExtra("photo",auth.currentUser?.photoUrl).toString()
        startActivity(i)

        auth = Firebase.auth
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle((account.idToken!!))
                }

            } catch (e: ApiException) {
                Log.d("RRR", "Api exeption")
            }
        }

        binding.btnSignIn.setOnClickListener {
            signInWithGoogle()
        }
        authState()
        val name = auth.currentUser?.displayName.toString()
        viewModel.setUserName(name)
        Log.d("RRR", "Sign In Activity")
        Log.d("RRR", viewModel.userName.value.toString())
    }

    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }

    private fun signInWithGoogle() {
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "You are logged in!", Toast.LENGTH_SHORT).show()
                authState()
            } else {
                Toast.makeText(this, "Errore!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun authState() {
        if (auth.currentUser != null) {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("username", auth.currentUser!!.displayName.toString())
            i.putExtra("email", auth.currentUser?.email.toString())
            i.putExtra("number",auth.currentUser?.phoneNumber.toString())
            i.putExtra("photo",auth.currentUser?.photoUrl).toString()
            startActivity(i)
        }
    }


    override fun onRestart() {
        super.onRestart()
        Log.d("RRR", "restart")
        auth.signOut()
//        if (auth.currentUser == null){
//            Log.d("RRR","succses")
//        }

    }

}