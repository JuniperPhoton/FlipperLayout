package com.juniperphoton.showcase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import com.juniperphoton.flipperlayout.FlipperLayout

class MainActivity : AppCompatActivity() {
    private lateinit var flipperLayout: FlipperLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flipperLayout = findViewById(R.id.flipper_layout)
        val prevView = findViewById<View>(R.id.prev_btn)
        val nextView = findViewById<View>(R.id.next_btn)
        val resetView = findViewById<View>(R.id.reset_btn)

        resetView.setOnClickListener {
            flipperLayout.next(0, false)
        }
        prevView.setOnClickListener {
            flipperLayout.previous()
        }
        nextView.setOnClickListener {
            flipperLayout.next()
        }

        (findViewById<Spinner>(R.id.spinner)).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                flipperLayout.next(position)
            }
        }

        (0 until flipperLayout.childCount)
                .map { flipperLayout.getChildAt(it) }
                .forEach {
                    it.setOnClickListener { v ->
                        flipperLayout.next()
                        Log.d("main", (v as Button).text.toString())
                    }
                }
    }
}
