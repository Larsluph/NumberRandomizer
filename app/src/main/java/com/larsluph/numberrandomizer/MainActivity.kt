package com.larsluph.numberrandomizer

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Math.random

class MainActivity : AppCompatActivity() {

    private var lowerBound = 1
        set(value) {
            Log.d("lowerBound", "$field -> $value")
            if (value > upperBound) {
                Log.d("lowerBound", getString(R.string.switch_boundaries))
                field = upperBound
                upperBound = value
            } else
                field = value
            updateTextBoundaries()
        }
    private var upperBound = 10
        set(value) {
            Log.d("upperBound", "$field -> $value")
            if (value < lowerBound) {
                Log.d("upperBound", getString(R.string.switch_boundaries))
                field = lowerBound
                lowerBound = value
            } else
                field = value
            updateTextBoundaries()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()

        val button = findViewById<TextView>(R.id.numberButton)
        updateButtonFromNum(randint(lowerBound, upperBound))
        button.setOnClickListener { startRolling() }
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { startRolling() }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.bound_selector -> {
                genPopup()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun rollOnce() {
        runOnUiThread { updateButtonFromNum(randint(lowerBound, upperBound)) }
    }

    private fun startRolling() {
        Thread(RollingThread(this, 800)).start()
    }

    private fun updateTextBoundaries() {
        findViewById<TextView>(R.id.textView).text = getString(R.string.text_bounds, lowerBound, upperBound)
    }

    private fun updateButtonFromNum(num: Int) {
        val button = findViewById<TextView>(R.id.numberButton)
        button.setTextColor(getColorFromNum(num))
        button.text = num.toString()
        Log.d("updateButtonFromNum", num.toString())
    }

    private fun getColorFromNum(num: Int): Int {
        val colorId = when (num % 10) {
            1 -> R.color.num_1
            2 -> R.color.num_2
            3 -> R.color.num_3
            4 -> R.color.num_4
            5 -> R.color.num_5
            6 -> R.color.num_6
            7 -> R.color.num_7
            8 -> R.color.num_8
            9 -> R.color.num_9
            else -> R.color.num_0
        }
        return ResourcesCompat.getColor(resources, colorId, null)
    }

    private fun randint(minBound: Int, maxBound: Int): Int {
        val rand = random()

        return (minBound + (rand * (maxBound+1 - minBound))).toInt()
    }

    private fun loadData() {
        val sp = getSharedPreferences(getString(R.string.packagename), MODE_PRIVATE)
        lowerBound = sp.getInt(getString(R.string.lowbound_key), 1)
        upperBound = sp.getInt(getString(R.string.highbound_key), 10)
    }

    private fun saveData() {
        val sp = getSharedPreferences(getString(R.string.packagename), MODE_PRIVATE).edit()
        sp.putInt(getString(R.string.lowbound_key), lowerBound)
        sp.putInt(getString(R.string.highbound_key), upperBound)
        sp.apply()
    }

    private fun genPopup() {
        val inputLow = EditText(this)
        inputLow.inputType = InputType.TYPE_CLASS_NUMBER
        inputLow.setText(lowerBound.toString(), TextView.BufferType.NORMAL)

        val inputHigh = EditText(this)
        inputHigh.inputType = InputType.TYPE_CLASS_NUMBER
        inputHigh.setText(upperBound.toString(), TextView.BufferType.NORMAL)

        AlertDialog.Builder(this)
                .setTitle(getString(R.string.popup_lowbound))
                .setView(inputLow)
                .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                    try { lowerBound = inputLow.text.toString().toInt() } catch (e: NumberFormatException) {}
                    AlertDialog.Builder(this)
                            .setTitle(getString(R.string.popup_highbound))
                            .setView(inputHigh)
                            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                                try { upperBound = inputHigh.text.toString().toInt() } catch (e: NumberFormatException) {}
                            }
                            .setNegativeButton(getString(android.R.string.cancel)) {dialog, _ -> dialog.cancel()}
                            .show()
                }
                .setNegativeButton(getString(android.R.string.cancel)) {dialog, _ -> dialog.cancel()}
                .show()
    }
}
