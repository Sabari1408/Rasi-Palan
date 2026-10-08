package com.example.data.repository

import java.util.Calendar
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Low-precision astronomical panchangam calculator (Lahiri ayanamsa, values taken at
 * sunrise ≈ 06:00 IST). Accuracy: Moon ≈ 0.3°, so tithi/nakshatra boundaries can be off by
 * up to ~40 minutes on the day they change. Good for daily reference, not for muhurtham-grade use.
 */
object PanchangamCalculator {

    data class Result(
        val tithiNumber: Int,      // 1..30 (15 = Pournami, 30 = Amavasai)
        val nakshatraIndex: Int,   // 0..26
        val yogaIndex: Int,        // 0..26
        val karanaIndex: Int,      // 0..59
        val moonRasiIndex: Int,    // 0..11
        val tamilMonthIndex: Int,  // 0..11 (Chithirai = 0)
        val tamilDay: Int,         // 1..32
        val tamilYearIndex: Int,   // 0..59 (Prabhava = 0)
        val sunriseMinutesIst: Int,
        val sunsetMinutesIst: Int
    )

    private fun norm(x: Double): Double = ((x % 360.0) + 360.0) % 360.0

    private fun julianDay(y: Int, m: Int, d: Int, hourUt: Double): Double {
        var yy = y
        var mm = m
        if (mm <= 2) {
            yy -= 1
            mm += 12
        }
        val a = floor(yy / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (yy + 4716)) + floor(30.6001 * (mm + 1)) + d + hourUt / 24.0 + b - 1524.5
    }

    private fun sunLongitude(jd: Double): Double {
        val t = (jd - 2451545.0) / 36525.0
        val l0 = 280.46646 + 36000.76983 * t
        val m = Math.toRadians(357.52911 + 35999.05029 * t)
        val c = (1.914602 - 0.004817 * t) * sin(m) + 0.019993 * sin(2 * m) + 0.000289 * sin(3 * m)
        return norm(l0 + c)
    }

    private fun moonLongitude(jd: Double): Double {
        val t = (jd - 2451545.0) / 36525.0
        val lp = 218.3164477 + 481267.88123421 * t
        val d = Math.toRadians(297.8501921 + 445267.1114034 * t)
        val m = Math.toRadians(357.5291092 + 35999.0502909 * t)
        val mp = Math.toRadians(134.9633964 + 477198.8675055 * t)
        val f = Math.toRadians(93.2720950 + 483202.0175233 * t)
        val lon = lp +
            6.288774 * sin(mp) +
            1.274027 * sin(2 * d - mp) +
            0.658314 * sin(2 * d) +
            0.213618 * sin(2 * mp) -
            0.185116 * sin(m) -
            0.114332 * sin(2 * f) +
            0.058793 * sin(2 * d - 2 * mp) +
            0.057066 * sin(2 * d - m - mp) +
            0.053322 * sin(2 * d + mp) +
            0.045758 * sin(2 * d - m) +
            0.040923 * sin(m - mp) -
            0.034720 * sin(d) -
            0.030383 * sin(m + mp)
        return norm(lon)
    }

    /** Lahiri ayanamsa (degrees), linear approximation around J2000. */
    private fun ayanamsa(jd: Double): Double = 23.853 + 0.013969 * ((jd - 2451545.0) / 365.25)

    private fun siderealSunSignAtSunset(cal: Calendar): Int {
        val jd = julianDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 12.5)
        return floor(norm(sunLongitude(jd) - ayanamsa(jd)) / 30.0).toInt().coerceIn(0, 11)
    }

    /** Sunrise/sunset in minutes after midnight IST for Chennai (13.08°N, 80.27°E). */
    private fun sunTimes(y: Int, m: Int, d: Int, dayOfYear: Int): Pair<Int, Int> {
        val lat = Math.toRadians(13.0827)
        val lon = 80.2707
        val jd = julianDay(y, m, d, 6.5)
        val lam = Math.toRadians(sunLongitude(jd))
        val decl = asin(sin(Math.toRadians(23.4393)) * sin(lam))
        val b = Math.toRadians(360.0 / 365.0 * (dayOfYear - 81))
        val eot = 9.87 * sin(2 * b) - 7.53 * cos(b) - 1.5 * sin(b)
        val noonUtMin = 720.0 - 4.0 * lon - eot
        val cosH = (sin(Math.toRadians(-0.833)) - sin(lat) * sin(decl)) / (cos(lat) * cos(decl))
        val h = Math.toDegrees(acos(cosH.coerceIn(-1.0, 1.0)))
        val rise = noonUtMin - 4.0 * h + 330.0
        val set = noonUtMin + 4.0 * h + 330.0
        return Pair(rise.roundToInt(), set.roundToInt())
    }

    fun compute(calendar: Calendar): Result {
        val y = calendar.get(Calendar.YEAR)
        val m = calendar.get(Calendar.MONTH) + 1
        val d = calendar.get(Calendar.DAY_OF_MONTH)

        // 06:00 IST = 00:30 UT
        val jd = julianDay(y, m, d, 0.5)
        val ay = ayanamsa(jd)
        val sun = sunLongitude(jd)
        val moon = moonLongitude(jd)

        val elongation = norm(moon - sun)
        val tithi = floor(elongation / 12.0).toInt() + 1
        val karana = floor(elongation / 6.0).toInt().coerceIn(0, 59)

        val moonSid = norm(moon - ay)
        val sunSid = norm(sun - ay)
        val nakshatra = floor(moonSid / (360.0 / 27.0)).toInt().coerceIn(0, 26)
        val yoga = floor(norm(moonSid + sunSid) / (360.0 / 27.0)).toInt().coerceIn(0, 26)
        val moonRasi = floor(moonSid / 30.0).toInt().coerceIn(0, 11)

        // Tamil solar month/day: a month starts on the day the Sun enters the sign before sunset.
        val sign = siderealSunSignAtSunset(calendar)
        var day = 1
        val probe = calendar.clone() as Calendar
        var steps = 0
        while (steps < 32) {
            probe.add(Calendar.DAY_OF_YEAR, -1)
            if (siderealSunSignAtSunset(probe) != sign) break
            day++
            steps++
        }

        // Tamil year (60-year cycle, Prabhava = 1987-88) changes at Chithirai 1 (mid-April).
        val yearBase = when {
            sign >= 9 -> y - 1                                   // Thai, Maasi, Panguni
            sign == 8 && m == 1 -> y - 1                         // Margazhi days in early January
            else -> y
        }
        val yearIndex = Math.floorMod(yearBase - 1987, 60)

        val (rise, set) = sunTimes(y, m, d, calendar.get(Calendar.DAY_OF_YEAR))

        return Result(
            tithiNumber = tithi.coerceIn(1, 30),
            nakshatraIndex = nakshatra,
            yogaIndex = yoga,
            karanaIndex = karana,
            moonRasiIndex = moonRasi,
            tamilMonthIndex = sign,
            tamilDay = day,
            tamilYearIndex = yearIndex,
            sunriseMinutesIst = rise,
            sunsetMinutesIst = set
        )
    }
}
