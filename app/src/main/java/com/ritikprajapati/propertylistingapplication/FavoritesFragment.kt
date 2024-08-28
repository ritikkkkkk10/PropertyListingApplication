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
            fetchFavorites{
                    favoriteIds ->
                // Fetch properties only after fetching the favorite IDs
                fetchFavoriteProperties(favoriteIds)
            }
        }, 200) // 3 seconds delay

        return view
    }

    private fun fetchFavorites(onComplete: (Set<String>) -> Unit) {
        val sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val uid = sharedPref.getString("UID", "")

        if (uid.isNullOrEmpty()) {
            Log.e("FavoritesFragment", "UID is null or empty")
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            return
        }

        val favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(uid)
        val favoriteIds = mutableSetOf<String>()

        favoritesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // Iterate over the children and add each propertyId to the set
                for (snapshot in dataSnapshot.children) {
                    snapshot.getValue(String::class.java)?.let { favoriteId ->
                        favoriteIds.add(favoriteId)
                    }
                }
                // Return the set of favoriteIds
                onComplete(favoriteIds)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
                Log.e("FirebaseError", "Failed to fetch favorites: ${databaseError.message}")
            }
        })
    }

        fun fetchFavoriteProperties(favoriteIds: Set<String>) {
            if (favoriteIds.isEmpty()) {
                Log.d("FavoritesFragment", "No favorite properties found.")
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                return
            }

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
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
