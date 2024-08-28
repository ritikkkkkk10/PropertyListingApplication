package com.ritikprajapati.propertylistingapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var propertyAdapter: PropertyAdapter
    private var propertyList: MutableList<Property> = mutableListOf()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var progressBar: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        progressBar = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        propertyAdapter = PropertyAdapter(propertyList)
        recyclerView.adapter = propertyAdapter

        progressBar.visibility = View.VISIBLE

        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("properties")

        Handler(Looper.getMainLooper()).postDelayed({
            fetchFavorites()
        }, 200) // 3 seconds delay

        return view
    }

    private fun fetchFavorites() {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("FAVORITES", Context.MODE_PRIVATE)
        val favoriteIds = sharedPreferences.getStringSet("favorites_list", mutableSetOf()) ?: mutableSetOf()

        Log.d("FavoritesFragment", "Favorite IDs from SharedPreferences: $favoriteIds")

        // Ensure that the favorites list is not empty
        if (favoriteIds.isEmpty()) {
            Log.d("FavoritesFragment", "No favorite properties found.")
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            return
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertyList.clear() // Clear previous data

                for (snapshot in dataSnapshot.children) {
                    val propertyId = snapshot.key // Get the unique propertyId
                    if (propertyId in favoriteIds) {
                        val property = snapshot.getValue(Property::class.java)
                        if (property != null) {
                            if (propertyId != null) {
                                property.propertyId = propertyId
                            }
                            propertyList.add(property)
                        }
                    }
                }

                Log.d("FavoritesFragment", "Fetched ${propertyList.size} favorite properties")
                propertyAdapter.notifyDataSetChanged()

                // Hide the progress bar and show the RecyclerView
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
                Log.e("FavoritesFragment", "Error fetching favorites", databaseError.toException())
            }
        })
    }


}
