package com.lackofdream.kc.hage

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.preference.EditTextPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.PreferenceFragmentCompat
import com.lackofdream.kc.hage.SettingsActivity.Companion.KEY_CURRENT_POINTS
import com.lackofdream.kc.hage.SettingsActivity.Companion.KEY_EXPECTED_POINTS
import com.lackofdream.kc.hage.SettingsActivity.Companion.KEY_FIRED_CANON
import com.lackofdream.kc.hage.SettingsActivity.Companion.KEY_WILL_FIRE_BONUS
import java.util.*


class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {


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
                findPreference<EditTextPreference>(key)!!.summary = String.format("%s", sharedPreferences?.getString(key, ""))
            }
            KEY_FIRED_CANON, KEY_WILL_FIRE_BONUS -> {
                updateSummary()
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.i("SF", "here")
        addPreferencesFromResource(R.xml.preference)
        findPreference<EditTextPreference>(KEY_CURRENT_POINTS)!!.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }
        findPreference<EditTextPreference>(KEY_EXPECTED_POINTS)!!.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }
        updateSummary()
    }

    fun getStringSetSummary(key: String, default: String): String {
        val list = ArrayList<String>(preferenceScreen.sharedPreferences.getStringSet(key, HashSet<String>())!!)
        Collections.sort(list)
        val sb = StringBuffer()
        for (item in list) {
            sb.append("${item}, ")
        }
        if (sb.length == 0) return default
        return sb.substring(0, sb.length - 2)
    }

    fun updateSummary() {
        findPreference<EditTextPreference>(KEY_CURRENT_POINTS)!!.summary = preferenceScreen.sharedPreferences.getString(KEY_CURRENT_POINTS, "")
        findPreference<EditTextPreference>(KEY_EXPECTED_POINTS)!!.summary = preferenceScreen.sharedPreferences.getString(KEY_EXPECTED_POINTS, "")
        findPreference<MultiSelectListPreference>(KEY_FIRED_CANON)!!.summary = getStringSetSummary(KEY_FIRED_CANON, "都憋着")
        findPreference<MultiSelectListPreference>(KEY_WILL_FIRE_BONUS)!!.summary = getStringSetSummary(KEY_WILL_FIRE_BONUS, "都不打")
    }


}
