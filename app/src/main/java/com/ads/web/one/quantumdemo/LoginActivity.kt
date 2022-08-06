package com.ads.web.one.quantumdemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var emailLogin: EditText
    private lateinit var passwordLogin: EditText
    private lateinit var googleImg: ImageView
    private lateinit var fbImg: ImageView
    private lateinit var loginText: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var registerText: TextView
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //link XML components
        emailLogin = findViewById(R.id.emailid)
        passwordLogin = findViewById(R.id.password)
        googleImg = findViewById(R.id.google_sign)
        sharedPref =
            getSharedPreferences("Quantum Login", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        registerText = findViewById(R.id.registerNow)
        fbImg = findViewById(R.id.fb_sign)
        loginText = findViewById(R.id.login)
        auth = FirebaseAuth.getInstance()
        //use google sign in client
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleImg.setOnClickListener {
            signInGoogle()
        }
        loginText.setOnClickListener {
            val em = emailLogin.text.trim().toString()
            val ps = passwordLogin.text.trim().toString()
            if (em.isEmpty()) {
                emailLogin.error = "Enter email address"
                emailLogin.requestFocus()
            } else if (ps.isEmpty()) {
                passwordLogin.error = "Enter password"
                passwordLogin.requestFocus()
            }else{
                val email=sharedPref.getString("email",null)
                val password=sharedPref.getString("password",null)
                if(email==em&&password==ps){
                    editor.putString("login","true")
                    editor.commit()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        registerText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
//create google sign in launcher
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }
//handling the results
    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }
//updating the UI according to result of google login
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}