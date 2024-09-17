package com.opsc7311poe.gourmetguru_opscpoe

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class Login : AppCompatActivity() {

    private lateinit var txtREmail: EditText
    private lateinit var txtRPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtNoAccount: TextView
    private lateinit var txtLoginSSo: TextView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize views
        txtREmail = findViewById(R.id.txtREmail)
        txtRPassword = findViewById(R.id.txtRPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtNoAccount = findViewById(R.id.txtNoAccount)
        txtLoginSSo = findViewById(R.id.txtLoginSSo)

        // Set up login button click
        btnLogin.setOnClickListener {
            val email = txtREmail.text.toString().trim()
            val password = txtRPassword.text.toString().trim()

            if (validateLogin(email, password)) {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Login successful, proceed to main activity
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            // Login failed, display error message
                            Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // Set up Google SSO login button click
        txtLoginSSo.setOnClickListener {
            signInWithGoogle()
        }

        // Handle "No account?" text click, redirect to registration page
        txtNoAccount.setOnClickListener {
            startActivity(Intent(this, Registration::class.java))
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)

        } catch (e: ApiException) {
            Log.e("TAG", "Google sign-in failed: ${e.message}")
            Log.e("TAG", "Error code: ${e.statusCode}")
            Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,
        null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                {
                    // SSO login successful, proceed to main activity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                // SSO login failed, handle error
                Log.w("TAG", "signInWithCredential:failure", task.exception)
                Toast.makeText(this, "SSO login failed", Toast.LENGTH_SHORT).show()
            }
            }
    }
    private fun validateLogin(email: String, password: String): Boolean {
        // Validate the email and password fields
        if (TextUtils.isEmpty(email)) {
            txtREmail.error = "Email is required."
            return false
        }

        if (TextUtils.isEmpty(password)) {
            txtRPassword.error = "Password is required."
            return false
        }

        if (password.length < 6) {
            txtRPassword.error = "Password must be at least 6 characters long."
            return false
        }

        return true
    }
}
