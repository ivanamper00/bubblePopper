package com.amper.bubble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import com.amper.bubblepopping.`interface`.OnListEndListener
import com.amper.bubblepopping.model.Products
import com.amper.bubblepopping.view.BubbleLayout
import org.firezenk.bubbleemitter.BubbleEmitterView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var bubble: BubbleLayout? = null
    var items = mutableListOf<Products>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        bubble = findViewById(R.id.bubble)
        for(i in 0..25){
            when (i) {
                3 -> items.add(Products("https://m.media-amazon.com/images/I/61z5MWOhFWL._AC_UX500_.jpg", i.toString()))
                else -> {
                    items.add(Products("", i.toString()))
                }
            }
        }
        bubble?.addList(items)

        for(i in 25..40){
            when (i) {
                40 -> items.add(Products("https://cache.mrporter.com/variants/images/3983529959609399/in/w1200_q80.jpg", i.toString()))
                else -> {
                    items.add(Products("", i.toString()))
                }
            }
        }

        Handler().postDelayed({
            bubble?.addList(items)
        }, 3000)
    }

}