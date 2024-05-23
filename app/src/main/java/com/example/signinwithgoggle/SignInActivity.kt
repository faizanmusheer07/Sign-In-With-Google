package com.example.signinwithgoggle

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.signinwithgoggle.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }
lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.donthavebutton.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
            finish()
        }
        binding.LoginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email  = binding.EmailLogin.editableText.toString().trim()
        val password  = binding.PasswordLogin.editableText.toString().trim()
        if ( email.isBlank() || password.isBlank()) {
            Toast.makeText(this@SignInActivity, "Please Fill All Details", Toast.LENGTH_SHORT)
                .show()
        }
         else{
             firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                 if (it.isSuccessful){
                     Toast.makeText(this@SignInActivity, "login SuccessFully", Toast.LENGTH_SHORT)
                 }
                  else{
                     Toast.makeText(this@SignInActivity, it.exception?.localizedMessage, Toast.LENGTH_SHORT)
                 }
             }
        }
    }
}