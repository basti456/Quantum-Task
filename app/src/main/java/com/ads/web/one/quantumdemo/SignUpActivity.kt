package com.ads.web.one.quantumdemo

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpText: TextView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userName: EditText
    private lateinit var emailIdSignUp: EditText
    private lateinit var passwordSignUp: EditText
    private lateinit var mobileSignUp: EditText
    private lateinit var check: CheckBox
    private lateinit var progressDialog: ProgressDialog
    private lateinit var signIn: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //link XML components
        signUpText = findViewById(R.id.txtSignUp)
        userName = findViewById(R.id.name)
        emailIdSignUp = findViewById(R.id.emailRegister)
        mobileSignUp = findViewById(R.id.mobileRegister)
        passwordSignUp = findViewById(R.id.passwordRegister)
        signIn = findViewById(R.id.register)
        //progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading File....")
        progressDialog.setCancelable(false)
        sharedPref =
            getSharedPreferences("Quantum Login", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        check = findViewById(R.id.check_agreement)
        signIn.setOnClickListener {
            val usersName = userName.text.trim().toString()
            val usersEmail = emailIdSignUp.text.trim().toString()
            val userMobile = mobileSignUp.text.trim().toString()
            val checkAgreement = check.isChecked
            val usersPassword = passwordSignUp.text.trim().toString()
            //validation process
            if (usersName.isEmpty()) {
                userName.error = "Enter your name"
                userName.requestFocus()
            } else if (usersEmail.isEmpty()) {
                emailIdSignUp.error = "Enter your email address"
                emailIdSignUp.requestFocus()

            } else if (userMobile.isEmpty()) {
                mobileSignUp.error = "Enter your mobile number"
                mobileSignUp.requestFocus()
            } else if (usersPassword.isEmpty()) {
                passwordSignUp.error = "Enter your password"
                passwordSignUp.requestFocus()
            } else if (!checkAgreement) {
                Toast.makeText(this, "Please agree to terms and conditions", Toast.LENGTH_SHORT)
                    .show()
            } else {
                progressDialog.show()
                editor.putString("email", usersEmail)
                editor.putString("password", usersPassword)
                editor.commit()
                progressDialog.dismiss()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
    }

    override fun onStart() {
        super.onStart()
        val ok = sharedPref.getString("login", null)
        //checking login state
        if (ok != null && ok == "true") {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}