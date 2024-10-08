package com.ritikprajapati.propertylistingapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.ritikprajapati.propertylistingapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isAdmin = sharedPref.getBoolean("isAdmin", false)

        if(isAdmin) {
            binding.enterProperty.setOnClickListener {startActivity(Intent(context, PropertyEditActivity::class.java))}
         }
        else {
            binding.enterProperty.setOnClickListener {
                Snackbar.make(binding.root, "Only admins allowed in this field!", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.cardViewProperties.setOnClickListener { startActivity(Intent(context, PropertyListActivity::class.java)) }

        return binding.root
    }


}