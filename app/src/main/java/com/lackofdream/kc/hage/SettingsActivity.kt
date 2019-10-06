package com.lackofdream.kc.hage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    companion object {
        const val KEY_CURRENT_POINTS = "CURRENT_POINTS_WITH_BONUS"
        const val KEY_EXPECTED_POINTS = "EXPECTED_POINTS"
        const val KEY_WILL_FIRE_BONUS = "WILL_FIRE_BONUS"
        const val KEY_FIRED_CANON = "FIRED_CANON"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Log.i("SETTINGACT", "add fragment...")
        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }
}