package com.beatabaczynska.fazyksiazyca

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Integer.parseInt
import java.util.*


class MainActivity : AppCompatActivity() {

    fun julday(year: Int, month: Int, day: Int) {
        if (year < 0) { year ++; }
        var jy = parseInt(year);
        var jm = parseInt(month) +1;
        if (month <= 2) {jy--;	jm += 12;	}
        var jul = Math.floor(365.25 *jy) + Math.floor(30.6001 * jm) + parseInt(day) + 1720995;
        if (day+31*(month+12*year) >= (15+31*(10+12*1582))) {
            ja = Math.floor(0.01 * jy);
            jul = jul + 2 - ja + Math.floor(0.25 * ja);
        }
        return jul;
    }

    fun Trig2(year: Int, month: Int, day: Int){

        val n = Math.floor(12.37 * (year - 1900 + (1.0 * month - 0.5) / 12.0))
        val RAD = 3.14159265 / 180.0
        val t = n / 1236.85
        val t2 = t * t
        val a = 359.2242 + 29.105356 * n
        val am = 306.0253 + 385.816918 * n + 0.010730 * t2
        var xtra = 0.75933 + 1.53058868 * n + (1.178e-4 - 1.55e-7 * t) * t2
        xtra += (0.1734 - 3.93e-4 * t) * Math.sin(RAD * a) - 0.4068 * Math.sin(
            RAD * am
        )
        val i = if (xtra > 0.0) Math.floor(xtra) else Math.ceil(xtra - 1.0)
        val j1 = julday(year, month, day)
        val jd = 2415020 + 28 * n + i
        return (j1 - jd + 30) % 30
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        tvDzisiaj.text = currentYear.toString() + " " + currentMonth.toString() + " " + currentDay.toString()


    }




}
