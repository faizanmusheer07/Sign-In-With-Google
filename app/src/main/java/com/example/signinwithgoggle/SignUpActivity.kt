@file:Suppress("DEPRECATION")

package com.example.signinwithgoggle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.signinwithgoggle.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class SignUpActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
            finish()
        }

        binding.SignUpButton1.setOnClickListener {
            signUp()
        }
        binding.google.setOnClickListener {
            val signInClient = googleSignInClient.signInIntent
           launcher.launch(signInClient)
        }
    }
private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
    result ->
    if (result.resultCode == Activity.RESULT_OK){
       val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
       if (task.isSuccessful){
           val account : GoogleSignInAccount?= task.result
           val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
           auth.signInWithCredential(credential).addOnCompleteListener {
               if (it.isSuccessful){
                   Toast.makeText(this@SignUpActivity, "Done", Toast.LENGTH_SHORT).show()
                   startActivity(Intent(this@SignUpActivity, MainActivity::class.java))

               }
                else{
                   Toast.makeText(this@SignUpActivity, "error", Toast.LENGTH_SHORT).show()
               }
           }
       }
    }
    else{
        Toast.makeText(this@SignUpActivity, "Failed", Toast.LENGTH_SHORT).show()
    }
}


    private fun signUp() {
        val name = binding.NameSign.editableText.toString().trim()
        val email = binding.EmailSign.editableText.toString().trim()
        val password = binding.PasswordSign.editableText.toString().trim()
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            Toast.makeText(this@SignUpActivity, "Please Fill All Details", Toast.LENGTH_SHORT)
                .show()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            this@SignUpActivity, it.exception?.localizedMessage, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

}


