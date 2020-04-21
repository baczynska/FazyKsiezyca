package com.beatabaczynska.fazyksiazyca

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    fun setSavedOptions() {
        algorithmsChoice.clearCheck()
        algorithmsChoice.check(algorithmsChoice[MoonAlgorithms.algo].id)
        hemisphereGroup.clearCheck()
        hemisphereGroup.check(hemisphereGroup[MoonAlgorithms.moonSide].id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        goBackButtonSettingsActivity.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val algorithms = MoonAlgorithms()
        val algorithmsArray: Array<String> = algorithms.algorithms
        for(a: String in algorithmsArray) {
            val rdbtn = RadioButton(this)
            rdbtn.id = View.generateViewId()
            rdbtn.text = a
            algorithmsChoice.addView(rdbtn)
        }
        setSavedOptions()

        algorithmsChoice.setOnCheckedChangeListener { _, i -> run {
            algorithms.onAlgoChanged(this, algorithmsChoice.indexOfChild(findViewById(i)).toString())
        }  }
        hemisphereGroup.setOnCheckedChangeListener { _, i -> run {
            algorithms.onHemisphereChanged(this, hemisphereGroup.indexOfChild(findViewById(i)).toString())
        }  }
    }
}
