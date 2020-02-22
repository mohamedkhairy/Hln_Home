package com.example.hlnhome

import android.os.Bundle
import com.example.hlnhome.home.HomeFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startFragment()
    }


    private fun startFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_view, HomeFragment())
            .commit()
    }



}
