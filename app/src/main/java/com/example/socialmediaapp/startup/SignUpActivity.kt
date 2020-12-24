package com.example.socialmediaapp.startup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firebaseAuth = FirebaseAuth.getInstance()
        signinButtonSU.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {

        val email = emailInputSU.text.toString().trim()
        val pass = passwordInputSU.text.toString().trim()
        val confPass = confirmPasswordInputSU.text.toString().trim()

        if(email == "" || pass == "" || confPass == "") {
            resetFields()
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        if(!email.contains("@") && !email.contains(".")) {
            resetFields()
            Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show()
            return
        }
        if(pass.length < 6) {
            resetFields()
            Toast.makeText(this, "Enter at least 6 characters for password", Toast.LENGTH_SHORT).show()
            return
        }
        if(pass != confPass) {
            resetFields()
            Toast.makeText(this, "passwords fields doesn't match", Toast.LENGTH_SHORT).show()
            return
        }
        progressBarSU.visibility = View.VISIBLE

        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        Log.d("success", "createUserWithEmail:success")
                        progressBarSU.visibility = View.GONE
                        startActivity(Intent(this, SignUpInfoActivity::class.java))
                    } else {
                        Log.d("success", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "user creating failed", Toast.LENGTH_SHORT).show()
                        progressBarSU.visibility = View.GONE
                    }
                })
    }
    private fun resetFields() {
        emailInputSU.setText("")
        passwordInputSU.setText("")
        confirmPasswordInputSU.setText("")
    }
}