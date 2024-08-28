package com.ritikprajapati.propertylistingapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class PropertyListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var propertyAdapter: PropertyAdapter
    private var propertyList: MutableList<Property> = mutableListOf()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var progressBar: View
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_list)

        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        propertyAdapter = PropertyAdapter(propertyList)
        recyclerView.adapter = propertyAdapter

        progressBar.visibility = View.VISIBLE

        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("properties")

        Handler(Looper.getMainLooper()).postDelayed({
            fetchProperties()
        }, 200) // 3 seconds delay

        setupSearchView()
    }

    private fun fetchProperties() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertyList.clear() // Clear previous data
                for (snapshot in dataSnapshot.children) {
                    val propertyId = snapshot.key // Get the unique propertyId
                    val property = snapshot.getValue(Property::class.java)
                    if (propertyId != null) {
                        property?.propertyId = propertyId
                    } // Assign the propertyId
                    property?.let { propertyList.add(it) }
                }
                Log.d("PropertyList", "Fetched ${propertyList.size} properties")
                propertyAdapter = PropertyAdapter(propertyList)
                recyclerView.adapter = propertyAdapter

                // Hide the progress bar and show the RecyclerView
                        progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterProperties(newText)
                return true
            }
        })
    }
    private fun filterProperties(query: String?) {
        val filteredList = propertyList.filter { property ->
            property.propertyName.contains(query ?: "", ignoreCase = true) ||
                    property.location.contains(query ?: "", ignoreCase = true) ||
                    property.price.contains(query ?: "", ignoreCase = true)
        }

        propertyAdapter = PropertyAdapter(filteredList)
        recyclerView.adapter = propertyAdapter
    }
}
