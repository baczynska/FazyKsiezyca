package com.beatabaczynska.fazyksiazyca

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    val counter: MoonAlgorithms = MoonAlgorithms()

    fun roundPercentage(percMoon: Int): Int {
        val nearestPercentage: Int

        if(percMoon < 5){
            nearestPercentage = 0
        } else if (percMoon < 15){
            nearestPercentage = 10
        }else if (percMoon < 25){
            nearestPercentage = 20
        }else if (percMoon < 35){
            nearestPercentage = 30
        }else if (percMoon < 45){
            nearestPercentage = 40
        }else if (percMoon < 55){
            nearestPercentage = 50
        }else if (percMoon < 65){
            nearestPercentage = 60
        }else if (percMoon < 75){
            nearestPercentage = 70
        }else if (percMoon < 85){
            nearestPercentage = 80
        }else if (percMoon < 95){
            nearestPercentage = 90
        }else {
            nearestPercentage = 100
        }
        return nearestPercentage
    }

    fun setPicture(percBetweenMoon: Int, percMoon: Int) {
        val ending: Char
        if(percBetweenMoon <= 50){
            ending = 'n'
        } else {
            ending = 'p'
        }

        val nearestPercentage: Int = this.roundPercentage(percMoon)
        val imageFileName = counter.decodedMoonSide() + "_" + nearestPercentage.toString() + "_" + ending
        val id: Int = getResources().getIdentifier("com.beatabaczynska.fazyksiazyca:drawable/$imageFileName", "jpg", null );

        imageView.setImageResource(id)
    }

    fun setPreviousAndNextNewMoonLabel() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        var currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        var currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        var todayVal = counter.getDayValue(currentYear, currentMonth, currentDay)

        var percMoon = counter.toPercentage100(todayVal).toInt()
        var percBetweenMoon = counter.toPercentage50(todayVal).toInt()

        tvDzisiaj.text = "Dzisiaj: ${percBetweenMoon}%"

        var fromBack = counter.lookBack(currentYear, currentMonth, currentDay)

        if(todayVal<=15.0){
            // kiedy poprzedni nów
            tvPoprzedniNow.text =
                "Poprzedni nów: ${fromBack.dayOfMonth}.${fromBack.monthValue}.${fromBack.getYear()} r."
        } else {
            // kiedy poprzednia pełnia
            tvPoprzedniNow.text =
                "Poprzednia pełnia: ${fromBack.dayOfMonth}.${fromBack.monthValue}.${fromBack.getYear()} r."
        }

        val fromForward = counter.lookForward(currentYear, currentMonth, currentDay)

        if(todayVal<15.0){
            // kiedy następna pełnia
            tvNastepnaPelnia.text =
                "Nastepna pelnia: ${fromForward.dayOfMonth}.${fromForward.monthValue}.${fromForward.getYear()} r."
        } else {
            // kiedy nastepny nów
            tvNastepnaPelnia.text =
                "Następny nów: ${fromForward.dayOfMonth}.${fromForward.monthValue}.${fromForward.getYear()} r."
        }

        setPicture(100 - percBetweenMoon, percMoon)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        counter.loadAlgo(this)
        counter.loadSemisphere(this)

        fullMoonInYear.setOnClickListener {
            val intent = Intent(this, FullMoonList::class.java)
            startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        setPreviousAndNextNewMoonLabel()

    }
}
