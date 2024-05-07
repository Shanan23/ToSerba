package com.example.buynow.presentation.admin.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.buynow.R
import com.example.buynow.presentation.admin.fragment.HomeAdminFragment
import com.example.buynow.presentation.admin.fragment.ProductAdminFragment
import com.example.buynow.presentation.admin.fragment.UserAdminFragment
import com.example.buynow.presentation.user.fragment.RecentFragment
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
                val fragment = HomeAdminFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }

            R.id.productMenu -> {
                val fragment = ProductAdminFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }

            R.id.userMenu -> {
                val fragment = UserAdminFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }

            R.id.profileMenu -> {
                val fragment = RecentFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }
        }
        return false

    }
}