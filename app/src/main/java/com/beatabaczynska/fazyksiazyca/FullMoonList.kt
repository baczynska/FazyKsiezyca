package com.beatabaczynska.fazyksiazyca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_full_moon_list.*

class FullMoonList : AppCompatActivity() {

    private val counter = MoonAlgorithms()

    fun findFullMoons(year: Int) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, counter.getAllFullMoonsInYear(year))
        fullMoonList.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_moon_list)

        goBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        yearInput.doOnTextChanged { text, start, count, after -> run {
            if(text.toString().toInt() in 1900..2200){
                findFullMoons(text.toString().toInt())
            }
            }
        }

        plusButton.setOnClickListener {
            if(yearInput.text.toString() == ""){
                yearInput.setText("1")
            } else {
                yearInput.setText((yearInput.text.toString().toInt() + 1).toString())
            }
        }

        minusButton.setOnClickListener {
            if(yearInput.text.toString() == ""){
                yearInput.setText("0")
            } else if (yearInput.text.toString() != "0"){
                yearInput.setText((yearInput.text.toString().toInt() - 1).toString())
            }
        }
    }
}
