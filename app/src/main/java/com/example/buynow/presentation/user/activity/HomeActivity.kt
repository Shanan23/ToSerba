package com.example.buynow.presentation.user.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.buynow.R
import com.example.buynow.presentation.user.fragment.BagFragment
import com.example.buynow.presentation.user.fragment.RecentFragment
import com.example.buynow.presentation.user.fragment.HomeFragment
import com.example.buynow.presentation.user.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var bottomNavigationView:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById(R.id.bottomNavMenu)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.nav_fragment, HomeFragment() ).commit()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.homeMenu -> {
                val fragment = HomeFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }

            R.id.bagMenu -> {
                val fragment = BagFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }

            R.id.favMenu -> {
                val fragment = RecentFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }

            R.id.profileMenu -> {
                val fragment = ProfileFragment()
                supportFragmentManager.beginTransaction().replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }
        }
        return false
    }
}


