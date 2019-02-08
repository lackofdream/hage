package com.lackofdream.kc.hage

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v4.app.NavUtils
import android.view.MenuItem
import java.util.*


class SettingsActivity : AppCompatPreferenceActivity() {
    companion object {
        val KEY_CURRENT_POINTS = "CURRENT_POINTS_WITH_BONUS"
        val KEY_EXPECTED_POINTS = "EXPECTED_POINTS"
        val KEY_WILL_FIRE_BONUS = "WILL_FIRE_BONUS"
        val KEY_FIRED_CANON = "FIRED_CANON"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, HagePreferenceFragment())
                .commit()
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this)
            }
            return true
        }
        return super.onMenuItemSelected(featureId, item)
    }

    override fun isValidFragment(fragmentName: String): Boolean {
        return PreferenceFragment::class.java.name == fragmentName
                || HagePreferenceFragment::class.java.name == fragmentName
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class HagePreferenceFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {


        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            when (key) {
                KEY_CURRENT_POINTS, KEY_EXPECTED_POINTS -> {
                    findPreference(key).summary = String.format("%s", sharedPreferences?.getString(key, ""))
                }
                KEY_FIRED_CANON, KEY_WILL_FIRE_BONUS -> {
                    updateSummary()
                }
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preference)
            updateSummary()
        }

        fun getStringSetSummary(key: String, default: String): String {
            val list = ArrayList<String>(preferenceScreen.sharedPreferences.getStringSet(key, HashSet<String>()))
            Collections.sort(list)
            val sb = StringBuffer()
            for (item in list) {
                sb.append("${item}, ")
            }
            if (sb.length == 0) return default
            return sb.substring(0, sb.length - 2)
        }

        fun updateSummary() {
            findPreference(KEY_CURRENT_POINTS).summary = preferenceScreen.sharedPreferences.getString(KEY_CURRENT_POINTS, "")
            findPreference(KEY_EXPECTED_POINTS).summary = preferenceScreen.sharedPreferences.getString(KEY_EXPECTED_POINTS, "")
            findPreference(KEY_FIRED_CANON).summary = getStringSetSummary(KEY_FIRED_CANON, "都憋着")
            findPreference(KEY_WILL_FIRE_BONUS).summary = getStringSetSummary(KEY_WILL_FIRE_BONUS, "都不打")
        }
    }

}
