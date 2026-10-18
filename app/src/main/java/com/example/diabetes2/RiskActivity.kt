package com.example.diabetes2

import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RiskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risk)

        val ageGroup = findViewById<RadioGroup>(R.id.ageGroup)
        val familyGroup = findViewById<RadioGroup>(R.id.familyGroup)
        val activityGroup = findViewById<RadioGroup>(R.id.activityGroup)
        val glucoseGroup = findViewById<RadioGroup>(R.id.glucoseGroup)
        val riskResult = findViewById<TextView>(R.id.riskResult)

        findViewById<Button>(R.id.checkRisk).setOnClickListener {

            if (ageGroup.checkedRadioButtonId == -1 ||
                familyGroup.checkedRadioButtonId == -1 ||
                activityGroup.checkedRadioButtonId == -1 ||
                glucoseGroup.checkedRadioButtonId == -1
            ) {
                Toast.makeText(
                    this,
                    "Please answer all questions",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val messages = mutableListOf<String>()

            if (ageGroup.checkedRadioButtonId == R.id.age45plus) {
                messages.add("Age 45 or above is a recognized risk factor.")
            } else if (ageGroup.checkedRadioButtonId == R.id.age35to44) {
                messages.add("Risk can increase with age; consider discussing screening with a healthcare professional.")
            }

            if (familyGroup.checkedRadioButtonId == R.id.familyYes) {
                messages.add("A parent or sibling with diabetes can increase your risk.")
            }

            if (activityGroup.checkedRadioButtonId == R.id.activityNo) {
                messages.add("Increasing regular physical activity may help reduce type 2 diabetes risk.")
            }

            if (glucoseGroup.checkedRadioButtonId == R.id.glucoseYes) {
                messages.add("Follow up with a healthcare professional about your previous high blood glucose result.")
            } else if (glucoseGroup.checkedRadioButtonId == R.id.glucoseUnsure) {
                messages.add("A healthcare professional can advise whether blood glucose screening is appropriate.")
            }

            val resultText = if (messages.isEmpty()) {
                "Your answers did not identify the factors covered by this questionnaire. This does not mean you have no risk. Maintain healthy habits and follow recommended health check-ups."
            } else {
                "Factors to be aware of:\n\n" +
                        messages.joinToString("\n\n") +
                        "\n\nConsider discussing your personal risk and appropriate screening with a healthcare professional."
            }

            riskResult.text = resultText
        }
    }
}
