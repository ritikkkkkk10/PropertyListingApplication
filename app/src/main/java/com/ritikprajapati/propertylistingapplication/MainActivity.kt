package com.ritikprajapati.propertylistingapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.ritikprajapati.propertylistingapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuClickHandled = when (item.itemId) {
            R.id.logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Warning!")
                    .setMessage("Confirm logout")
                    .setPositiveButton("Yes") { _, _ ->
                        FirebaseAuth.getInstance().signOut();
                        val view = this@MainActivity.currentFocus ?: findViewById(android.R.id.content)
                        Snackbar.make(view, "Logged out", Snackbar.LENGTH_SHORT).show()
                        val intent = Intent(this, startActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
        return menuClickHandled
    }

    private fun navigateToLogin() {
        val intent = Intent(this, startActivity::class.java) // Change to your login activity
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear the activity stack
        startActivity(intent)
        finish() // Optional: Close the current activity
    }
}