package com.ritikprajapati.propertylistingapplication

import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("properties")

        fetchProperties()
    }

    private fun fetchProperties() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertyList.clear() // Clear previous data
                for (snapshot in dataSnapshot.children) {
                    val property = snapshot.getValue(Property::class.java)
                    property?.let { propertyList.add(it) }
                }
                Log.d("PropertyList", "Fetched ${propertyList.size} properties")
                propertyAdapter = PropertyAdapter(propertyList)
                recyclerView.adapter = propertyAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}
