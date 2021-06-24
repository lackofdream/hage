package com.lackofdream.kc.hage

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import babushkatext.BabushkaText
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                Log.i("MAINACT", "starting setting activity intent")
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun addRankPiece(babushkaText: BabushkaText, t1: String, t2: String, t3: String) {

        babushkaText.addPiece(BabushkaText.Piece.Builder(t1 + " ")
                .textColor(Color.parseColor("#414141"))
                .build())

        babushkaText.addPiece(BabushkaText.Piece.Builder(t2 + "\n")
                .textColor(Color.parseColor("#CC9A9A"))
                .textSizeRelative(0.5f)
                .build())

        babushkaText.addPiece(BabushkaText.Piece.Builder(t3 + "\n\n")
                .textColor(Color.parseColor("#FFCC00"))
                .textSizeRelative(1.5f)
                .build())
    }

    fun fillMainTextView() {

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val expectedPts = sharedPreferences.getString(SettingsActivity.KEY_EXPECTED_POINTS, "0")!!.toDouble()
        val willFireBonus = sharedPreferences.getStringSet(SettingsActivity.KEY_WILL_FIRE_BONUS, setOf())
        val firedCanons = sharedPreferences.getStringSet(SettingsActivity.KEY_FIRED_CANON, setOf())
        val currentPts = sharedPreferences.getString(SettingsActivity.KEY_CURRENT_POINTS, "0")!!.toDouble()

        val babushkaText = findViewById<BabushkaText>(R.id.pts_per_day)

        val df = DecimalFormat("#.##")

        babushkaText.reset()
        addRankPiece(babushkaText, "今日计划总战果", "(不包含bonus战果)",
                if (expectedPts > 1) df.format(Calculator.get_today_goal_without_bonus(expectedPts, willFireBonus!!))
                else "N/A"
        )

        addRankPiece(babushkaText, "今日实际总战果", "(不包含bonus战果)",
                df.format(Calculator.get_current_points_without_bonus(currentPts, firedCanons!!))
        )
        addRankPiece(babushkaText, "平均每日要肝", "(不包含bonus战果)",
                if (expectedPts > 1) df.format(Calculator.get_points_without_bonus_needed_per_day(expectedPts, willFireBonus!!))
                else "N/A"
        )
        addRankPiece(babushkaText, "剩余老本", "",
                if (expectedPts > 1) df.format(
                        Calculator.get_current_points_without_bonus(currentPts, firedCanons) -
                                Calculator.get_today_goal_without_bonus(expectedPts, willFireBonus!!))
                else "N/A")

        babushkaText.display()
    }


    override fun onResume() {
        super.onResume()

        fillMainTextView()
    }
}
