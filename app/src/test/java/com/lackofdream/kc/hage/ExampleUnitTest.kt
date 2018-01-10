package com.lackofdream.kc.hage

import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun get_current_points_without_bonus_isCorrect() {
        assertEquals(Calculator.get_current_points_without_bonus(252.0, setOf("1-5")), 177.0, 1e-1)
    }

}
