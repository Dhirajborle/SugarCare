
package com.example.diabetes2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<View>(R.id.btnHabits).setOnClickListener {
            startActivity(Intent(this, HabitsActivity::class.java))
        }


        findViewById<View>(R.id.btnBMI).setOnClickListener {
            startActivity(Intent(this, BMICalculatorActivity::class.java))
        }


        findViewById<View>(R.id.btnLearn).setOnClickListener {
            startActivity(Intent(this, EducationActivity::class.java))
        }


        findViewById<View>(R.id.btnRisk).setOnClickListener {
            startActivity(Intent(this, RiskActivity::class.java))
        }


    }
}
