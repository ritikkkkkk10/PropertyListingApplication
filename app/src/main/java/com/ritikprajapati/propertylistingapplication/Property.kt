package com.ritikprajapati.propertylistingapplication

data class Property(
    var propertyId: String = "",
    val propertyName: String = "",
    val location: String = "",
    val price: String = "",
    val shortDescription: String = "",
    val longDescription: String = "",
    val contactPhone: String = "",
    val contactEmail: String = "",
    val imageUrls: List<String> = emptyList() // List of image URLs
)
