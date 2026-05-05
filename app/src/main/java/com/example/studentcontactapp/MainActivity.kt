package com.example.studentcontactapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.studentcontactapp.utils.SettingsManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val settings = SettingsManager(this)

        if (settings.getDark()) {
            setTheme(android.R.style.Theme_DeviceDefault)
        } else {
            setTheme(android.R.style.Theme_DeviceDefault_Light)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nav = findViewById<BottomNavigationView>(R.id.bottomNav)

        if (savedInstanceState == null) {
            load(HomeFragment())
        }

        nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> load(HomeFragment())
                R.id.menu_search -> load(SearchFragment())
                R.id.menu_profile -> load(ProfileFragment())
                R.id.menu_notes -> load(NoteListFragment())
            }
            true
        }
    }

    private fun load(f: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, f)
            .commit()
    }
}