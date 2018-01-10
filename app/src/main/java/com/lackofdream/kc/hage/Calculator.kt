package com.lackofdream.kc.hage

import java.util.*

/**
 * Created by g on 18/01/03.
 */
class Calculator {
    companion object {
        val EO_BONUS = mapOf(
                "1-5" to 75,
                "1-6" to 75,
                "2-5" to 100,
                "3-5" to 150,
                "4-5" to 180,
                "5-5" to 200,
                "6-5" to 250
        )
        val Z_BONUS = 350

        fun get_days_in_month(): Double {
            return GregorianCalendar().getActualMaximum(Calendar.DAY_OF_MONTH).toDouble()
        }

        fun get_need_points_without_bonus(expectedPts: Double, willFireZ: Boolean): Double {
            var ret = expectedPts
            for (item in EO_BONUS.entries) {
                ret -= item.value
            }
            if (willFireZ) {
                ret -= Z_BONUS
            }
            return ret
        }

        fun get_points_without_bonus_needed_per_day(expectedPts: Double, willFireZ: Boolean): Double {
            return get_need_points_without_bonus(expectedPts, willFireZ) / (get_days_in_month() - 1)
        }

        fun get_current_points_without_bonus(currentPts: Double, firedCanons: Set<String>): Double {
            var ret = currentPts
            for (item in firedCanons) {
                if (item.equals("Z炮")) ret -= Z_BONUS
                else ret -= EO_BONUS[item] ?: 0
            }
            return ret
        }

        fun get_today_goal_without_bonus(expectedPts: Double, willFireZ: Boolean): Double {
            return GregorianCalendar().get(Calendar.DAY_OF_MONTH) * get_points_without_bonus_needed_per_day(expectedPts, willFireZ)
        }

    }

}