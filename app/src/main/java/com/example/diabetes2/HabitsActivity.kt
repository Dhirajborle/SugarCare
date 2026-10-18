
package com.example.diabetes2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HabitsActivity : AppCompatActivity() {

    private lateinit var prefs: android.content.SharedPreferences

    private lateinit var setupStatus: TextView
    private lateinit var targetPanel: View
    private lateinit var dailyPanel: View
    private lateinit var summaryPanel: View

    private lateinit var targetStep: TextView
    private lateinit var targetQuestion: TextView
    private lateinit var targetUnit: TextView
    private lateinit var targetTip: TextView
    private lateinit var targetInput: EditText
    private lateinit var targetProgress: ProgressBar
    private lateinit var nextTarget: Button
    private lateinit var backTarget: Button

    private lateinit var dailyStep: TextView
    private lateinit var dailyQuestion: TextView
    private lateinit var dailyUnit: TextView
    private lateinit var dailyInput: EditText
    private lateinit var dailyResult: TextView
    private lateinit var dailyProgress: ProgressBar
    private lateinit var nextDaily: Button
    private lateinit var backDaily: Button

    private lateinit var summaryResult: TextView

    private var targetIndex = 0
    private var dailyIndex = 0
    private var editingTargets = false

    private val names = listOf(
        "Water intake",
        "Calories",
        "Carbohydrates",
        "Protein",
        "Fibre",
        "Fruit and vegetable servings",
        "Physical activity",
        "Sleep"
    )

    private val questions = listOf(
        "How much water do you aim to drink daily?",
        "What is your daily calorie target?",
        "What is your daily carbohydrate target?",
        "What is your daily protein target?",
        "What is your daily fibre target?",
        "How many fruit and vegetable servings do you aim for?",
        "How many minutes of physical activity do you aim for?",
        "How many hours of sleep do you aim for?"
    )

    private val units = listOf(
        "Litres per day",
        "Kilocalories (kcal) per day",
        "Grams per day",
        "Grams per day",
        "Grams per day",
        "Servings per day",
        "Minutes per day",
        "Hours per night"
    )

    private val tips = listOf(
        "Hydration needs vary with weather, activity and health conditions.",
        "Energy needs depend on age, body size, activity and health goals.",
        "Carbohydrate needs are individual. Avoid using a generic target as medical advice.",
        "Protein needs vary with body size, activity and health conditions.",
        "Choose fibre-rich foods such as vegetables, legumes and whole grains.",
        "Use a realistic target suited to your diet and local food choices.",
        "Choose a safe activity goal that matches your current fitness and abilities.",
        "Set a realistic sleep goal that supports your daily routine."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habits)

        prefs = getSharedPreferences("SugarCareStepTracker", MODE_PRIVATE)

        setupStatus = findViewById(R.id.setupStatus)
        targetPanel = findViewById(R.id.targetPanel)
        dailyPanel = findViewById(R.id.dailyPanel)
        summaryPanel = findViewById(R.id.summaryPanel)

        targetStep = findViewById(R.id.targetStep)
        targetQuestion = findViewById(R.id.targetQuestion)
        targetUnit = findViewById(R.id.targetUnit)
        targetTip = findViewById(R.id.targetTip)
        targetInput = findViewById(R.id.targetInput)
        targetProgress = findViewById(R.id.targetProgress)
        nextTarget = findViewById(R.id.nextTarget)
        backTarget = findViewById(R.id.backTarget)

        dailyStep = findViewById(R.id.dailyStep)
        dailyQuestion = findViewById(R.id.dailyQuestion)
        dailyUnit = findViewById(R.id.dailyUnit)
        dailyInput = findViewById(R.id.dailyInput)
        dailyResult = findViewById(R.id.dailyResult)
        dailyProgress = findViewById(R.id.dailyProgress)
        nextDaily = findViewById(R.id.nextDaily)
        backDaily = findViewById(R.id.backDaily)

        summaryResult = findViewById(R.id.summaryResult)

        findViewById<TextView>(R.id.todayDate).text =
            SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
                .format(Date())

        nextTarget.setOnClickListener { saveTargetStep() }

        backTarget.setOnClickListener {
            if (targetIndex > 0) {
                targetIndex--
                showTargetQuestion()
            }
        }

        nextDaily.setOnClickListener { saveDailyStep() }

        backDaily.setOnClickListener {
            if (dailyIndex > 0) {
                dailyIndex--
                showDailyQuestion()
            }
        }

        findViewById<Button>(R.id.editTargets).setOnClickListener {
            editingTargets = true
            targetIndex = 0
            dailyPanel.visibility = View.GONE
            summaryPanel.visibility = View.GONE
            targetPanel.visibility = View.VISIBLE
            showTargetQuestion()
        }

        findViewById<Button>(R.id.restartDaily).setOnClickListener {
            startDailyTracking()
        }

        loadToday()
    }

    private fun loadToday() {
        val today = todayKey()

        if (prefs.getString("dailyDate", "") != today) {
            prefs.edit()
                .putString("dailyDate", today)
                .apply()
        }

        if (prefs.getBoolean("targetsSaved", false)) {
            setupStatus.text = "Your personal targets are saved on this device."
            startDailyTracking()
        } else {
            targetPanel.visibility = View.VISIBLE
            dailyPanel.visibility = View.GONE
            summaryPanel.visibility = View.GONE
            targetIndex = 0
            editingTargets = false
            showTargetQuestion()
        }
    }

    private fun showTargetQuestion() {
        targetStep.text = "TARGET ${targetIndex + 1} OF ${names.size}"
        targetProgress.progress = targetIndex + 1
        targetQuestion.text = questions[targetIndex]
        targetUnit.text = units[targetIndex]
        targetTip.text = tips[targetIndex]

        targetInput.setText(
            prefs.getString("target$targetIndex", "")
        )

        nextTarget.text =
            if (targetIndex == names.lastIndex) "Save Targets" else "Next"

        backTarget.visibility =
            if (targetIndex == 0) View.GONE else View.VISIBLE
    }

    private fun saveTargetStep() {
        val value = targetInput.text.toString().trim().toDoubleOrNull()

        if (value == null || !value.isFinite() || value <= 0) {
            targetInput.error = "Enter a number greater than zero"
            return
        }

        prefs.edit()
            .putString("target$targetIndex", value.toString())
            .apply()

        if (targetIndex < names.lastIndex) {
            targetIndex++
            showTargetQuestion()
        } else {
            prefs.edit()
                .putBoolean("targetsSaved", true)
                .apply()

            editingTargets = false
            Toast.makeText(this, "Targets saved!", Toast.LENGTH_SHORT).show()
            startDailyTracking()
        }
    }

    private fun startDailyTracking() {
        targetPanel.visibility = View.GONE
        dailyPanel.visibility = View.VISIBLE
        summaryPanel.visibility = View.GONE
        dailyIndex = 0
        showDailyQuestion()
    }

    private fun showDailyQuestion() {
        dailyStep.text = "ITEM ${dailyIndex + 1} OF ${names.size}"
        dailyProgress.max = 100
        dailyQuestion.text = "How much ${names[dailyIndex].lowercase()} have you recorded today?"
        dailyUnit.text = units[dailyIndex]

        dailyInput.setText(
            prefs.getString("intake$dailyIndex", "")
        )

        nextDaily.text =
            if (dailyIndex == names.lastIndex) "Save Today's Intake" else "Next"

        backDaily.visibility =
            if (dailyIndex == 0) View.GONE else View.VISIBLE

        updateCurrentResult()
    }

    private fun updateCurrentResult() {
        val amount = dailyInput.text.toString().toDoubleOrNull()
        val target = prefs.getString("target$dailyIndex", "")?.toDoubleOrNull()

        if (amount == null || target == null || target <= 0) {
            dailyResult.text = "Enter today's amount to see your progress."
            dailyProgress.progress = 0
            return
        }

        val percent = ((amount / target) * 100).toInt()
        dailyProgress.progress = percent.coerceIn(0, 100)

        dailyResult.text =
            "Recorded: $amount ${units[dailyIndex]}\n" +
                    "Your target: $target ${units[dailyIndex]}\n" +
                    "Progress: $percent%"
    }

    private fun saveDailyStep() {
        val raw = dailyInput.text.toString().trim()
        val amount = raw.toDoubleOrNull()

        if (amount == null || !amount.isFinite() || amount < 0) {
            dailyInput.error = "Enter zero or a positive number"
            return
        }

        prefs.edit()
            .putString("intake$dailyIndex", amount.toString())
            .apply()

        if (dailyIndex < names.lastIndex) {
            dailyIndex++
            showDailyQuestion()
        } else {
            dailyPanel.visibility = View.GONE
            summaryPanel.visibility = View.VISIBLE
            showSummary()
            Toast.makeText(
                this,
                "Today's intake saved",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showSummary() {
        val summary = StringBuilder()
        var total = 0
        var count = 0

        names.forEachIndexed { index, name ->
            val amount = prefs.getString("intake$index", "")?.toDoubleOrNull()
            val target = prefs.getString("target$index", "")?.toDoubleOrNull()

            if (amount != null && target != null && target > 0) {
                val percent = ((amount / target) * 100).toInt()
                summary.append(
                    "$name\n$amount / $target ${units[index]}\nProgress: $percent%\n\n"
                )
                total += percent.coerceAtMost(100)
                count++
            }
        }

        if (count > 0) {
            summary.insert(
                0,
                "Average target progress: ${total / count}%\n\n"
            )
        }

        summaryResult.text = summary.toString().trim()
    }

    private fun todayKey(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())
    }
}
