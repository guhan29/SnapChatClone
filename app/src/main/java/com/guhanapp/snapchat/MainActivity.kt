package com.guhanapp.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null

    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        if(mAuth.currentUser != null) {
            logIn();
        }
    }

    fun goClicked(view: View) {
        // Check if we can login
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        logIn()
                    } else {
                        mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Add to database
                                        //val database = Firebase.database
                                        Firebase.database.getReference().child("users").child(task.result?.user!!.uid).child("email").setValue(emailEditText?.text.toString())
                                        logIn();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(this, "Login Failed. Try Again", Toast.LENGTH_SHORT).show()
                                    }
                                }
                    }
                }

        // SignUp
    }

    fun logIn() {
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }
}