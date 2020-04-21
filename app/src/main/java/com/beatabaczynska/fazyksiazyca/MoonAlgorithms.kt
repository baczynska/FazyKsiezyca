package com.beatabaczynska.fazyksiazyca

import android.content.Context
import android.widget.Toast
import java.time.LocalDate
import java.time.Period
import java.util.*
import kotlin.math.roundToInt

class MoonAlgorithms {

    //  1 (or anything else) -> Simple; 2 -> Conway; 3 -> Trig1; 4-> Trig2;
    //  1 (or anything else) -> N; 2 -> S;
    companion object {
        var algo = 1
        var moonSide = 1
    }

    fun decodedMoonSide(): Char {
        if(moonSide == 1) {
            return 'n'
        }
        return 's'
    }

    fun saveInFile(context: Context, filename: String, value: String) {
        try {
            val fos = context.openFileOutput(filename, Context.MODE_PRIVATE).also {
                it.write(value.toByteArray())
            }
            fos.close()
        } catch (t: Throwable) {
            Toast.makeText(context, "Nie udało się zapisać ustawień", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readFromFile(context: Context, filename: String) : String {
        try{
            context.openFileInput(filename).bufferedReader().useLines { return it.toList()[0] }
        } catch (ignore: Throwable){
            return "1"
        }
    }

    fun loadAlgo(context: Context): Int {
        algo = readFromFile(context,"algorithm").toInt()
        return algo
    }

    fun loadSemisphere(context: Context): Int {
        moonSide = readFromFile(context,"hemisphere").toInt()
        return moonSide
    }

    fun onAlgoChanged(context: Context, option: String) {
        algo = option.toInt()
        saveInFile(context,"algorithm", option)
    }

    fun onHemisphereChanged(context: Context, option: String) {
        moonSide = option.toInt()
        saveInFile(context,"hemisphere", option)
    }

    val algorithms: Array<String> = arrayOf("Simple", "Conway", "Trig1", "Trig2")
        get() : Array<String> {
            return field
        }

    fun getAllFullMoonsInYear(year: Int): Array<String?> {
        val list = ArrayList<String>()

        val date = Calendar.getInstance()
        date.set(year,0,1)
        while(date.get(Calendar.YEAR) == year) {
            if(getDayValue(year,date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH)).roundToInt() in 0..2) {
                list.add("${date.get(Calendar.DAY_OF_MONTH)}.${date.get(Calendar.MONTH) + 1}.${year}")
                date.add(Calendar.DATE, 23)
            }
            date.add(Calendar.DATE, 1)
        }
        val array = arrayOfNulls<String>(list.size)
        list.toArray(array)
        return array
    }

    fun getDayValue(year: Int, month: Int, day: Int): Double {
        return when (algo){
            2 -> Conway(year, month, day)
            3 -> Trig1(year, month, day)
            4 -> Trig2(year, month, day)
            else -> Simple(year, month, day)
        }
    }

    fun julday(yearI: Int, monthI: Int, dayI: Int): Double {
        var year = yearI;
        var month = monthI;
        var day = dayI;
        if (year < 0) { year ++; }
        var jy = year;
        var jm = month + 1;
        if (month <= 2) {jy--;	jm += 12;	}
        var jul = Math.floor(365.25 *jy) + Math.floor(30.6001 * jm) + day + 1720995;
        if (day+31*(month+12*year) >= (15+31*(10+12*1582))) {
            var ja = Math.floor(0.01 * jy);
            jul = jul + 2 - ja + Math.floor(0.25 * ja);
        }
        return jul;
    }

    fun GetFrac(fr: Double): Double {	return (fr - Math.floor(fr));}

    fun Simple(year: Int, month: Int, day: Int): Double
    {
        var lp = 2551443;
        var now = Date(year, month-1, day, 20, 35, 0);
        var new_moon = Date(1970, 0, 7, 20, 35, 0);
        var phase = ((now.getTime() - new_moon.getTime())/1000) % lp;
        return Math.floor((phase /(24*3600)).toDouble()) + 1;
    }

    fun Conway(year: Int, month: Int, day: Int): Double {
        var r = year % 100.0;
        r %= 19;
        if (r>9){ r -= 19;}
        r = ((r * 11) % 30) + month + day
        if (month<3){r += 2;}
        if (year<2000) r = r - 4 else r = r - 8.3
        r = Math.floor(r+0.5)%30;
        if (r < 0) return r+30 else return r;
    }

    fun Trig1(year: Int,month: Int,day: Int): Double {
        var thisJD = julday(year,month,day);
        var degToRad = 3.14159265 / 180;
        var K0 = Math.floor((year-1900)*12.3685);
        var T = (year-1899.5) / 100;
        var T2 = T*T;
        var T3 = T*T*T;
        var J0 = 2415020 + 29*K0;
        var F0 = 0.0001178*T2 - 0.000000155*T3 + (0.75933 + 0.53058868*K0) - (0.000837*T + 0.000335*T2);
        var M0 = 360*(GetFrac(K0*0.08084821133)) + 359.2242 - 0.0000333*T2 - 0.00000347*T3;
        var M1 = 360*(GetFrac(K0*0.07171366128)) + 306.0253 + 0.0107306*T2 + 0.00001236*T3;
        var B1 = 360*(GetFrac(K0*0.08519585128)) + 21.2964 - (0.0016528*T2) - (0.00000239*T3);
        var phase = 0
        var jday = 0.0
        var oldJ = 0.0
        while (jday < thisJD) {
            var F = F0 + 1.530588*phase;
            var M5 = (M0 + phase*29.10535608)*degToRad;
            var M6 = (M1 + phase*385.81691806)*degToRad;
            var B6 = (B1 + phase*390.67050646)*degToRad;
            F -= 0.4068*Math.sin(M6) + (0.1734 - 0.000393*T)*Math.sin(M5);
            F += 0.0161*Math.sin(2*M6) + 0.0104*Math.sin(2*B6);
            F -= 0.0074*Math.sin(M5 - M6) - 0.0051*Math.sin(M5 + M6);
            F += 0.0021*Math.sin(2*M5) + 0.0010*Math.sin(2*B6-M6);
            F += 0.5 / 1440;
            oldJ = jday;
            jday = J0 + 28*phase + Math.floor(F);
            phase++;
        }
        return (thisJD - oldJ)%30;
    }



    fun Trig2(year: Int, month: Int, day: Int): Double {

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

    fun toPercentage100(number: Double): Double{
        if(number == 0.0) return 0.0
        if(number == 15.0) return 100.0
        if( number < 15.0) return number*(100.0/15)
        else  return (100.0 - (number - 15.0)*(100.0/15))
    }

    fun toPercentage50(number: Double): Double{
        if(number == 0.0) return 0.0
        if(number == 29.0) return 100.0
        else  return (number*(100.0/30.0))
    }

    fun lookBack(year: Int, month: Int, day: Int): LocalDate {
        // wait for 0.0 or 15.0
        var ealierDay = LocalDate.of(year, month, day)
        var res = 11.1
        do{
            ealierDay = ealierDay.minus(Period.ofDays(1))
            when (algo){
                1 -> res = Trig1(ealierDay.year, ealierDay.monthValue, ealierDay.dayOfMonth)
                2 -> res = Trig2(ealierDay.year, ealierDay.monthValue, ealierDay.dayOfMonth)
                3 -> res = Conway(ealierDay.year, ealierDay.monthValue, ealierDay.dayOfMonth)
                else -> res = Simple(ealierDay.year, ealierDay.monthValue, ealierDay.dayOfMonth)
            }
        } while((res != 0.0) and (res!= 15.0) and (res!=30.0))

        return ealierDay
    }

    fun lookForward(year: Int, month: Int, day: Int): LocalDate {
        // wait for 0.0 or 15.0
        var nextDay = LocalDate.of(year, month, day)
        var res = 11.1
        do{
            nextDay = nextDay.plus(Period.ofDays(1))
            when (algo){
                1 -> res = Trig1(nextDay.year, nextDay.monthValue, nextDay.dayOfMonth)
                2 -> res = Trig2(nextDay.year, nextDay.monthValue, nextDay.dayOfMonth)
                3 -> res = Conway(nextDay.year, nextDay.monthValue, nextDay.dayOfMonth)
                else -> res = Simple(nextDay.year, nextDay.monthValue, nextDay.dayOfMonth)
            }
        } while((res != 0.0) and (res!= 15.0) and (res!= 30.0))

        return nextDay
    }
}