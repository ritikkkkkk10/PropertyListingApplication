package com.ritikprajapati.propertylistingapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PropertyEditActivity : AppCompatActivity() {

    private lateinit var propertyName: EditText
    private lateinit var location: EditText
    private lateinit var price: EditText
    private lateinit var shortDescription: EditText
    private lateinit var longDescription: EditText
    private lateinit var contactPhone: EditText
    private lateinit var contactEmail: EditText

    private lateinit var imageContainer: LinearLayout
    private lateinit var uploadImageButton: Button
    private lateinit var savePropertyButton: Button
    private lateinit var progressBar: ProgressBar

    private val storageRef: StorageReference =
        FirebaseStorage.getInstance().reference.child("property_images")

    private val PICK_IMAGE_REQUEST = 1 // Request code for image selection
    private val maxImages = 5
    private val imageUris = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_edit)

        propertyName = findViewById(R.id.propertyName)
        location = findViewById(R.id.location)
        price = findViewById(R.id.price)
        shortDescription = findViewById(R.id.shortDescription)
        longDescription = findViewById(R.id.longDescription)
        contactPhone = findViewById(R.id.contactPhone)
        contactEmail = findViewById(R.id.contactEmail)

        imageContainer = findViewById(R.id.imageContainer)
        uploadImageButton = findViewById(R.id.uploadImage)
        savePropertyButton = findViewById(R.id.saveProperty)
        progressBar = findViewById(R.id.progressBar)

        uploadImageButton.setOnClickListener {
            if (imageUris.size < maxImages) {
                openGallery()
            } else {
                Snackbar.make(it, "Maximum number of images reached", Snackbar.LENGTH_SHORT).show()
            }
        }

        savePropertyButton.setOnClickListener {
            savePropertyDetails()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*" // Filter to show only images
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                if (imageUris.size < maxImages) {
                    imageUris.add(uri)
                    updateImageViews()
                }
            }
        }
    }

    private fun updateImageViews() {
        val imageViews = listOf(
            findViewById<ImageView>(R.id.image1),
            findViewById<ImageView>(R.id.image2),
            findViewById<ImageView>(R.id.image3),
            findViewById<ImageView>(R.id.image4),
            findViewById<ImageView>(R.id.image5)
        )
        imageViews.forEachIndexed { index, imageView ->
            if (index < imageUris.size) {
                imageView.visibility = ImageView.VISIBLE
                imageView.setImageURI(imageUris[index])
            } else {
                imageView.visibility = ImageView.GONE
            }
        }
    }

    private fun savePropertyDetails() {

        progressBar.visibility = ProgressBar.VISIBLE

        if (imageUris.isEmpty()) {
            Snackbar.make(savePropertyButton, "Please upload at least one image", Snackbar.LENGTH_SHORT).show()
            progressBar.visibility = ProgressBar.GONE
            return
        }

        val propertyData = mapOf(
            "propertyName" to propertyName.text.toString(),
            "location" to location.text.toString(),
            "price" to price.text.toString(),
            "shortDescription" to shortDescription.text.toString(),
            "longDescription" to longDescription.text.toString(),
            "contactPhone" to contactPhone.text.toString(),
            "contactEmail" to contactEmail.text.toString()
        )
        val propertyRef: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("properties")
        val propertyId = propertyRef.push().key ?: return

        propertyRef.child(propertyId).setValue(propertyData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                uploadImages(propertyId)

            } else {
                // Hide the ProgressBar if saving fails
                progressBar.visibility = ProgressBar.GONE
                Snackbar.make(savePropertyButton, "Failed to save property details", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImages(propertyId: String) {
        val imageUrls = mutableListOf<String>()

        val imageUploadTasks = imageUris.mapIndexed { index, uri ->
            val imageRef = storageRef.child("$propertyId/image_$index.jpg")
            imageRef.putFile(uri).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let { imageUrl ->
                        imageUrls.add(imageUrl.toString())
                        if (imageUrls.size == imageUris.size) {
                            updatePropertyImages(propertyId, imageUrls)
                        }
                    }
                } else {
                    // Hide the ProgressBar if image upload fails
                    progressBar.visibility = ProgressBar.GONE
                    Snackbar.make(savePropertyButton, "Failed to upload images", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updatePropertyImages(propertyId: String, imageUrls: List<String>) {
        val propertyRef: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("properties")
        propertyRef.child(propertyId).child("imageUrls").setValue(imageUrls).addOnCompleteListener { task ->
            progressBar.visibility = ProgressBar.GONE
            if (task.isSuccessful) {
                Snackbar.make(savePropertyButton, "Property saved successfully", Snackbar.LENGTH_SHORT).show()

                // Delay finishing the activity to allow the Snackbar to be visible
                Handler(Looper.getMainLooper()).postDelayed({
                    finish() // Finish the activity after the Snackbar is shown
                }, 1500) // Delay of 1.5 seconds
            } else {
                Snackbar.make(savePropertyButton, "Failed to update image URLs", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}
