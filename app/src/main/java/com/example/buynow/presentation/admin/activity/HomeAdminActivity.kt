package com.example.buynow.presentation.admin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.buynow.R
import com.example.buynow.presentation.admin.fragment.HomeAdminFragment
import com.example.buynow.presentation.user.fragment.BagFragment
import com.example.buynow.presentation.user.fragment.FavFragment
import com.example.buynow.presentation.user.fragment.HomeFragment
import com.example.buynow.presentation.user.fragment.ProfileFragment
import com.example.buynow.presentation.user.fragment.ShopFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeAdminActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        bottomNavigationView = findViewById(R.id.bottomNavMenu)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.nav_fragment, HomeAdminFragment() ).commit()
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

            R.id.productMenu -> {
                val fragment = ShopFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }

            R.id.userMenu -> {
                val fragment = BagFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }

            R.id.profileMenu -> {
                val fragment = FavFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }
        }
        return false

    }
}