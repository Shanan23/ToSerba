package com.example.buynow.presentation.seller.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.buynow.R
import com.example.buynow.presentation.seller.fragment.HomeSellerFragment
import com.example.buynow.presentation.seller.fragment.ProductSellerFragment
import com.example.buynow.presentation.seller.fragment.UserSellerFragment
import com.example.buynow.presentation.user.fragment.ProfileFragment
import com.example.buynow.presentation.user.fragment.RecentFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeSellerActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        bottomNavigationView = findViewById(R.id.bottomNavMenu)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.nav_fragment, HomeSellerFragment() ).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.homeMenu -> {
                val fragment = HomeSellerFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }

            R.id.productMenu -> {
                val fragment = ProductSellerFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }

            R.id.userMenu -> {
                val fragment = UserSellerFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
                    .commit()
                return true
            }

            R.id.profileMenu -> {
                finish()

                return true
            }
        }
        return false

    }
}