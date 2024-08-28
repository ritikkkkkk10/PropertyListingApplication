package com.ritikprajapati.propertylistingapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private var radioGroupAdmin: RadioGroup? = null
    private var radioYes: RadioButton? = null
    private var radioNo: RadioButton? = null
    private lateinit var progressBar: ProgressBar

    var firstName: TextInputEditText? = null
    var lastName: TextInputEditText? = null
    var email: TextInputEditText? = null
    var phone: TextInputEditText? = null
    var password: TextInputEditText? = null
    var register: Button? = null
    private var auth: FirebaseAuth? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        firstName = findViewById(R.id.edit_text_first_name)
        lastName = findViewById(R.id.edit_text_last_name)
        email = findViewById(R.id.edit_text_email)
        phone = findViewById(R.id.edit_text_phone)
        password = findViewById(R.id.edit_text_password)
        register = findViewById(R.id.button_create_account)
        auth = FirebaseAuth.getInstance()

        radioGroupAdmin = findViewById(R.id.radioGroupAdmin)
        radioYes = findViewById(R.id.radioYes)
        radioNo = findViewById(R.id.radioNo)
        progressBar = findViewById(R.id.progressBar)


        val registerButton = register
        val txt_email = email
        val txt_password = password
        val txt_phone = phone

        registerButton?.setOnClickListener(View.OnClickListener {

            progressBar.visibility = ProgressBar.VISIBLE
            hideKeyboard()

            val emailText = txt_email?.text.toString()
            val passwordText = txt_password?.text.toString()
            val phoneText = txt_phone?.text.toString();
            if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(
                    phoneText
                )
            ) {
                progressBar.visibility = ProgressBar.GONE
                Snackbar.make(
                    this@RegisterActivity.currentFocus!!,
                    "Missing Credentials!",
                    Snackbar.LENGTH_SHORT
                ).show()
        }
            else if (passwordText.length < 6) {
                progressBar.visibility = ProgressBar.GONE
                Snackbar.make(this@RegisterActivity.currentFocus!!, "Password too short!", Snackbar.LENGTH_SHORT).show()
            } else {
                val isAdmin = getAdminSelection()
                registerUser( passwordText, phoneText, emailText, isAdmin)
            }
        })
    }

    private fun getAdminSelection(): Boolean {
        return when (radioGroupAdmin?.checkedRadioButtonId) {
            R.id.radioYes -> true
            R.id.radioNo -> false
            else -> false
        }
    }

    private fun registerUser(txtPassword: String, txtPhone: String, txtEmail: String, isAdmin: Boolean) {
        auth!!.createUserWithEmailAndPassword(txtEmail, txtPassword)
            .addOnCompleteListener(this@RegisterActivity) { task ->
                //hello
                if (task.isSuccessful) {
                    val currentUser = auth!!.currentUser
                    val userUid = currentUser?.uid

                    // Store user details along with UID in your database
                    // For example, you can store them in Firebase Realtime Database or Firestore
                    // Here, we'll assume you're using Firebase Realtime Database
                    userUid?.let { uid ->

                        var name = firstName!!.text.toString() + " " + lastName!!.text.toString()

                        val userData = mapOf(
                            "firstName" to firstName!!.text.toString(),
                            "lastName" to lastName!!.text.toString(),
                            "email" to txtEmail,
                            "phone" to txtPhone,
                            "isAdmin" to isAdmin
                        )

                        saveUidToSharedPreferences(uid, name, txtEmail, txtPhone, isAdmin)

                        FirebaseDatabase.getInstance().getReference("newUsers")
                            .child(uid)
                            .setValue(userData)
                            .addOnSuccessListener {
                                Snackbar.make(
                                    this@RegisterActivity.currentFocus!!,
                                    "Registering user successful!",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { exception ->
                                Snackbar.make(
                                    this@RegisterActivity.currentFocus!!,
                                    "Registration failed!: ${exception.message}",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
                else {
                    progressBar.visibility = ProgressBar.GONE
                    Snackbar.make(
                        this@RegisterActivity.currentFocus!!,
                        "Registration failed!: ${task.exception?.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
    }
    private fun saveUidToSharedPreferences(uid: String,name: String, email: String, phone: String, isAdmin: Boolean) {
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("UID", uid)
        editor.putString("Name", name)
        editor.putString("Email", email)
        editor.putString("Phone", phone)
        editor.putBoolean("isAdmin", isAdmin)
        editor.apply()
    }

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
