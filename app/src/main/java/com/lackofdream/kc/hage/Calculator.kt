package com.lackofdream.kc.hage

import java.util.*

/**
 * Created by g on 18/01/03.
 */
class Calculator {
    companion object {
        val BONUS = mapOf(
                "1-5" to 75,
                "1-6" to 75,
                "2-5" to 100,
                "3-5" to 150,
                "4-5" to 180,
                "5-5" to 200,
                "6-5" to 250,
                "Z炮" to 350,
                "南西炮" to 80,
                "西方炮" to 330,
                "Z后炮" to 400,
                "三川炮" to 200,
                "泊地炮" to 300
        )

        fun get_days_in_month(): Double {
            return GregorianCalendar().getActualMaximum(Calendar.DAY_OF_MONTH).toDouble() - 2
        }

        fun get_need_points_without_bonus(
                expectedPts: Double,
                willFireBonus: Set<String>): Double {
            var ret = expectedPts
            for (task in willFireBonus) {
                ret -= BONUS[task] ?: 0
            }
            return ret
        }

        fun get_points_without_bonus_needed_per_day(expectedPts: Double, willFireBonus: Set<String>): Double {
            return get_need_points_without_bonus(expectedPts, willFireBonus) / (get_days_in_month() - 1)
        }

        fun get_current_points_without_bonus(currentPts: Double, firedCanons: Set<String>): Double {
            var ret = currentPts
            for (item in firedCanons) {
                ret -= BONUS[item] ?: 0
            }
            return ret
        }

        fun get_today_goal_without_bonus(expectedPts: Double, willFireBonus: Set<String>): Double {
            return GregorianCalendar().get(Calendar.DAY_OF_MONTH) * get_points_without_bonus_needed_per_day(expectedPts, willFireBonus)
        }

    }

}