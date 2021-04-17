package com.example.cowsandbulls

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var counter = 0

        val mCurrentArrayEditText = findViewById<EditText>(R.id.et_current)
        val mCheckButton = findViewById<Button>(R.id.button_check)
        val mOutputTextView = findViewById<TextView>(R.id.tv_output)
        val mCounterTextView = findViewById<TextView>(R.id.tv_counter)
        val mDroppingButton = findViewById<Button>(R.id.button_dropping)

        var targetArray = getRandomArray()

        mCheckButton.setOnClickListener {
            var currentArray: List<Int> = ArrayList()
            try {
                currentArray = mCurrentArrayEditText.text.map { it.toString().toInt() }
            } catch (ex: Exception){
                Toast.makeText(this, "Try to enter number again", Toast.LENGTH_SHORT).show()
            }
            mOutputTextView.text = checkCowsAndBulls(targetArray, currentArray)
            counter++
            mCounterTextView.text = "Number of attempts: $counter"
        }

        mDroppingButton.setOnClickListener {
            counter = 0
            mCounterTextView.text = "Number of attempts: $counter"
            targetArray = getRandomArray()
            mCurrentArrayEditText.text.clear()
            mOutputTextView.text = ""
        }

    }

    private fun checkCowsAndBulls(targetArray: List<Int>, currentArray: List<Int>) : String{
        var numberOfCows = currentArray.count { targetArray.contains(it) }
        var numberOfBulls = 0

        for((index, it) in currentArray.withIndex()){
            if(it == targetArray[index]){
                numberOfBulls++
                numberOfCows--
            }
        }
        return "Cows: $numberOfCows, Bulls: $numberOfBulls"
    }

    private fun getRandomArray() : List<Int>{
        val array: MutableList<Int> = ArrayList()
        for(i in 0..3){
            var currentNumber: Int
            do{
                currentNumber = if(i == 0) Random().nextInt(9) + 1 else Random().nextInt(10)
            } while (array.contains(currentNumber))
            array.add(i, currentNumber)
        }
        println(array)
        return array
    }
}