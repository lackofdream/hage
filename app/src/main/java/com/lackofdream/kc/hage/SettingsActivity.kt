package com.lackofdream.kc.hage

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.PreferenceFragment
import android.support.v4.app.NavUtils
import android.view.MenuItem


class SettingsActivity : AppCompatPreferenceActivity() {
    companion object {
        val KEY_CURRENT_POINTS = "CURRENT_POINTS_WITH_BONUS"
        val KEY_EXPECTED_POINTS = "EXPECTED_POINTS"
        val KEY_WILL_FIRE_Z = "WILL_FIRE_Z"
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
                KEY_WILL_FIRE_Z -> {
                    val pref = findPreference(key) as CheckBoxPreference
                    val sharedPref = sharedPreferences?.getBoolean(key, false) == true
                    if (pref.isChecked != sharedPref)
                        pref.isChecked = sharedPref
                }
                KEY_CURRENT_POINTS, KEY_EXPECTED_POINTS -> {
                    findPreference(key).summary = String.format("%s", sharedPreferences?.getString(key, ""))
                }
                KEY_FIRED_CANON -> {
                    findPreference(key).summary = genFiredCanonSummary()
                    if (firedZ()) {
                        val editor = sharedPreferences?.edit()
                        editor?.putBoolean(KEY_WILL_FIRE_Z, true)
                        editor?.apply()
                    }
                    updateLogic()
                }
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preference)
            updateLogic()
            initSummary()
        }

        fun firedZ(): Boolean {
            return "Z炮" in preferenceScreen.sharedPreferences.getStringSet(KEY_FIRED_CANON, HashSet<String>())
        }

        fun updateLogic() {
            findPreference(KEY_WILL_FIRE_Z).isEnabled = !firedZ()
        }

        fun genFiredCanonSummary(): String {
            val set = preferenceScreen.sharedPreferences.getStringSet(KEY_FIRED_CANON, HashSet<String>())
            val sb = StringBuffer()
            for (item in set) {
                sb.append("${item}, ")
            }
            if (sb.length == 0) return "都憋着"
            return sb.substring(0, sb.length - 2)

        }

        fun initSummary() {
            findPreference(KEY_CURRENT_POINTS).summary = preferenceScreen.sharedPreferences.getString(KEY_CURRENT_POINTS, "")
            findPreference(KEY_EXPECTED_POINTS).summary = preferenceScreen.sharedPreferences.getString(KEY_EXPECTED_POINTS, "")
            findPreference(KEY_FIRED_CANON).summary = genFiredCanonSummary()
        }
    }

}
