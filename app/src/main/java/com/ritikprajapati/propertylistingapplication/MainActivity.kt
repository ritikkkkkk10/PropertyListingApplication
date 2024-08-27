package com.ritikprajapati.propertylistingapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.ritikprajapati.propertylistingapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var auth: FirebaseAuth? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.pager.adapter = ScreenSlidePagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Favourites"
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuClickHandled = when (item.itemId) {
            R.id.logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Warning!")
                    .setMessage("Confirm logout")
                    .setPositiveButton("Yes") { _, _ ->
                        FirebaseAuth.getInstance().signOut();
                        val view = this@MainActivity.currentFocus ?: findViewById(android.R.id.content)
                        Snackbar.make(view, "Logged out", Snackbar.LENGTH_SHORT).show()
                        val intent = Intent(this, startActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
        return menuClickHandled
    }
    private class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                else -> FavoritesFragment()
            }
        }
    }
}