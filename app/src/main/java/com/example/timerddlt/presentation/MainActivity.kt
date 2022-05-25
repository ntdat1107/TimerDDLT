package com.example.timerddlt.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.marginEnd
import androidx.drawerlayout.widget.DrawerLayout
import com.example.timerddlt.R
import com.example.timerddlt.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var binding: ActivityMainBinding? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        drawerLayout = binding?.drawerLayout!!
        navigationView = binding?.navView!!
        setSupportActionBar(binding?.toolbarHome!!)

        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding?.toolbarHome,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setCheckedItem(R.id.nav_home)
        navigationView.setNavigationItemSelectedListener(this)

        binding?.btnStart!!.setOnClickListener {
            binding?.toolbarHome!!.navigationIcon = null
            binding?.tvTitleMargin!!.visibility = View.GONE
            binding?.tvTitleNoMargin!!.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.close()
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_timeline -> {
                startActivity(Intent(this, TimelineActivity::class.java))
            }
        }
        drawerLayout.close()
        return true
    }
}
