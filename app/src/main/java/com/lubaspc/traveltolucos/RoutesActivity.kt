package com.lubaspc.traveltolucos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lubaspc.traveltolucos.ui.fragment.RoutesFragment

class RoutesActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.routes_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container,RoutesFragment())
            .commit()
    }
}