package com.example.exclusive


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

import com.example.exclusive.ui.slider.contract.SliderContract
import com.example.exclusive.ui.slider.model.SlidelViewModel
import com.example.exclusive.ui.slider.model.SliderModal
import com.example.exclusive.ui.slider.view.SliderAdapter

class IntroSliderActivity : AppCompatActivity(), SliderContract.View {

    private lateinit var presenter: SliderContract.Presenter
    private lateinit var skip: Button
    private lateinit var viewPager: ViewPager
    private lateinit var dotsLL: LinearLayout
    private lateinit var dots: Array<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_slider)

        val preferences: SharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
        val firstTime: String? = preferences.getString("FirstInstall", "")
        if (firstTime == "no") {
            navigateToMain()
        } else {
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.putString("FirstInstall", "no")
            editor.apply()
        }

        viewPager = findViewById(R.id.idViewPager)
        dotsLL = findViewById(R.id.idLLDots)
        presenter = SlidelViewModel(this)
        presenter.loadSliderData()
        skip = findViewById(R.id.idBtnSkip)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                for (i in dots.indices) {
                    if (i == position) {
                        dots[i].setTextColor(resources.getColor(R.color.primary_color))
                    } else {
                        dots[i].setTextColor(resources.getColor(android.R.color.black))
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        skip.setOnClickListener {
            presenter.onSkipClicked()
        }
    }

    override fun showSlider(sliders: ArrayList<SliderModal>) {
        val adapter = SliderAdapter(this, sliders)
        viewPager.adapter = adapter
        addDots(sliders.size, 0)
    }

    override fun navigateToMain() {
        val intent = Intent(this, AuthMain::class.java)
        startActivity(intent)
        finish()
    }

    private fun addDots(size: Int, pos: Int) {
        dots = Array(size) { TextView(this) }
        dotsLL.removeAllViews()
        dotsLL.gravity = Gravity.CENTER_HORIZONTAL
        for (i in 0 until size) {
            dots[i] = TextView(this)
            dots[i].text = "●"
            dots[i].textSize = 30F
            dots[i].setTextColor(resources.getColor(R.color.primary_color))
            dotsLL.addView(dots[i])
        }
        if (dots.isNotEmpty()) {
            dots[pos].setTextColor(resources.getColor(android.R.color.black))
        }
    }
}
