package edu.vt.cs5254.fancygallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavHost
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import edu.vt.cs5254.fancygallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        // standard bottom navigation:
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHost
        binding.bottomNav.setupWithNavController(navHost.navController)

        // required to allow navigation beyond bottom navigation (Phase 2+3):
        binding.bottomNav.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navHost.navController)
            true //this tells the bottom nav that it's done its job!
        }

        // OPTIONAL(not in assignment page) to better handle reselecting via bottom nav
        binding.bottomNav.setOnItemReselectedListener { item ->
            navHost.navController.popBackStack(item.itemId, false)
        }
        setContentView(binding.root)
    }
}