package com.ritikprajapati.propertylistingapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class startActivity : AppCompatActivity() {
    private var register: Button? = null
    private var login: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        register = findViewById(R.id.button_signup)
        login = findViewById(R.id.button_login)

        val registerButton = register
        val loginButton = login
        registerButton?.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@startActivity, RegisterActivity::class.java))
            finish()
        })
        loginButton?.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@startActivity, LoginActivity::class.java))
        })
    }

    override fun onStart() {
        super.onStart()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {
            startActivity(
                Intent(this@startActivity,
                    MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            finish()
        } else {
            val rootView = findViewById<View>(android.R.id.content)
            Snackbar.make(rootView, "Not registered!", Snackbar.LENGTH_SHORT).show()
        }
    }
}