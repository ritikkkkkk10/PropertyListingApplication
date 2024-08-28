package com.ritikprajapati.propertylistingapplication

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.ritikprajapati.propertylistingapplication.databinding.ActivityPropertyDetailBinding

class PropertyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val uid = sharedPref.getString("UID", "")

        // Retrieve data from intent
        val propertyId = intent.getStringExtra("propertyId") // Ensure propertyId is passed in Intent
        val propertyName = intent.getStringExtra("propertyName")
        val location = intent.getStringExtra("location")
        val price = intent.getStringExtra("price")
        val shortDescription = intent.getStringExtra("shortDescription")
        val longDescription = intent.getStringExtra("longDescription")
        val contactPhone = intent.getStringExtra("contactPhone")
        val contactEmail = intent.getStringExtra("contactEmail")
        val imageUrls = intent.getStringArrayListExtra("imageUrls")

        // Log retrieved values
        Log.d("PropertyDetailActivity", "propertyId: $propertyId")
        Log.d("PropertyDetailActivity", "propertyName: $propertyName")
        Log.d("PropertyDetailActivity", "location: $location")
        Log.d("PropertyDetailActivity", "price: $price")
        Log.d("PropertyDetailActivity", "shortDescription: $shortDescription")
        Log.d("PropertyDetailActivity", "longDescription: $longDescription")
        Log.d("PropertyDetailActivity", "contactPhone: $contactPhone")
        Log.d("PropertyDetailActivity", "contactEmail: $contactEmail")
        Log.d("PropertyDetailActivity", "imageUrls: $imageUrls")

        binding.propertyName.text = propertyName
        binding.location.text = location
        binding.price.text = price
        binding.shortDescription.text = shortDescription
        binding.longDescription.text = longDescription
        binding.contactPhone.text = contactPhone
        binding.contactEmail.text = contactEmail

        // Load images if available
        if (imageUrls != null) {
            // Use Glide to load images
            if (imageUrls.isNotEmpty()) {
                Glide.with(this)
                    .load(imageUrls.getOrNull(0)) // Load the first image
                    .into(binding.image1)
            }
            if (imageUrls.size > 1) {
                Glide.with(this)
                    .load(imageUrls.getOrNull(1)) // Load the second image
                    .into(binding.image2)
            }
            if (imageUrls.size > 2) {
                Glide.with(this)
                    .load(imageUrls.getOrNull(2)) // Load the third image
                    .into(binding.image3)
            }
        }

        binding.favorites.setOnClickListener {
            if (propertyId != null) {
                saveToFavorites(propertyId)
                saveToDatabaseFavorites(uid, propertyId)
            }
        }
    }

    private fun saveToDatabaseFavorites(uid: String?, propertyId: String) {
        // Ensure uid is not null
        if (uid == null) return

        // Get a reference to the Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()

        // Reference to the favorites node under the user's uid
        val favoritesRef = database.getReference("favorites").child(uid)

        // Store the propertyId under the user's favorites
        // Use propertyId as the key with a value of true (or any value you prefer)
        favoritesRef.push().setValue(propertyId)
            .addOnSuccessListener {
                // Handle success (e.g., show a message to the user)
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Handle failure (e.g., show an error message to the user)
                Toast.makeText(this, "Failed to add to favorites: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun saveToFavorites(propertyId: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("FAVORITES", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val favorites = sharedPreferences.getStringSet("favorites_list", mutableSetOf()) ?: mutableSetOf()

        if (favorites.contains(propertyId)) {
            Toast.makeText(this, "Property is already in favorites", Toast.LENGTH_SHORT).show()
        } else {
            favorites.add(propertyId)
            editor.putStringSet("favorites_list", favorites)
            editor.apply()
            Toast.makeText(this, "Property added to favorites", Toast.LENGTH_SHORT).show()
        }
    }

}