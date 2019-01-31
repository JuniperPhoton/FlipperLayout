package com.juniperphoton.showcase

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.juniperphoton.flipperlayout.FlipperLayout

class MainActivity : AppCompatActivity() {
    private lateinit var flipperLayout: FlipperLayout

    private var currentRefreshIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flipperLayout = findViewById(R.id.flipper_layout)
        val prevView = findViewById<View>(R.id.prev_btn)
        val nextView = findViewById<View>(R.id.next_btn)
        val resetView = findViewById<View>(R.id.reset_btn)
        val refresh = findViewById<View>(R.id.refresh_btn)

        resetView.setOnClickListener {
            flipperLayout.next(0, false)
        }
        prevView.setOnClickListener {
            flipperLayout.previous()
        }
        nextView.setOnClickListener {
            var next = flipperLayout.displayIndex + 1
            if (next >= flipperLayout.childCount) {
                next = 0
            }
            flipperLayout.next(
                    nextIndex = next,
                    animate = true,
                    endBlock = {
                        Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show()
                    })
        }
        refresh.setOnClickListener {
            flipperLayout.refreshCurrent {
                val child: TextView = flipperLayout.getCurrentView()
                child.text = "refresh index: ${++currentRefreshIndex}"
            }
        }

        findViewById<Spinner>(R.id.spinner).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (flipperLayout.displayIndex != position) {
                    flipperLayout.next(position)
                }
            }
        }

        (0 until flipperLayout.childCount)
                .map { flipperLayout.getChildAt(it) }
                .forEach {
                    it.setOnClickListener { v ->
                        flipperLayout.next()
                        Log.d("main", (v as TextView).text.toString())
                    }
                }
    }
}
