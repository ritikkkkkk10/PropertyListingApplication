package com.ritikprajapati.propertylistingapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var signout: Button
    private var auth: FirebaseAuth? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signout = findViewById(R.id.logout)
        auth = FirebaseAuth.getInstance()

        signout.setOnClickListener {
            auth?.signOut() // Sign out the user
            navigateToLogin() // Navigate to the login screen
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, startActivity::class.java) // Change to your login activity
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear the activity stack
        startActivity(intent)
        finish() // Optional: Close the current activity
    }
}