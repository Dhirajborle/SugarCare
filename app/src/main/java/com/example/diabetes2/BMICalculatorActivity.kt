
package com.example.diabetes2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class BMICalculatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        val heightField = findViewById<EditText>(R.id.heightInput)
        val weightField = findViewById<EditText>(R.id.weightInput)
        val calculateButton = findViewById<Button>(R.id.calculateBMI)
        val resultText = findViewById<TextView>(R.id.bmiResult)
        val categoryText = findViewById<TextView>(R.id.bmiCategory)

        calculateButton.setOnClickListener {
            val height = heightField.text.toString().trim().toDoubleOrNull()
            val weight = weightField.text.toString().trim().toDoubleOrNull()

            if (height == null || weight == null ||
                height <= 0 || weight <= 0 ||
                height > 300 || weight > 500
            ) {
                Toast.makeText(
                    this,
                    "Enter valid height and weight",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val heightInMeters = height / 100.0
                val bmi = weight / (heightInMeters * heightInMeters)

                val category = when {
                    bmi < 18.5 -> "Underweight"
                    bmi < 25.0 -> "Healthy weight"
                    bmi < 30.0 -> "Overweight"
                    else -> "Obesity"
                }

                resultText.text = String.format(Locale.US, "%.1f", bmi)
                categoryText.text = category
            }
        }
    }
}
