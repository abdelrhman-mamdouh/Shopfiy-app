package com.example.exclusive.ui.slider.view


import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.exclusive.R

import com.example.exclusive.ui.slider.contract.SliderContract
import com.example.exclusive.ui.slider.model.SlidelViewModel
import com.example.exclusive.ui.slider.model.SliderModal

class IntroSliderFragment : Fragment(), SliderContract.View {

    private lateinit var presenter: SliderContract.Presenter
    private lateinit var skip: Button
    private lateinit var viewPager: ViewPager
    private lateinit var dotsLL: LinearLayout
    private lateinit var dots: Array<TextView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_intro_slider, container, false)

        val preferences: SharedPreferences = requireActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
        val firstTime: String? = preferences.getString("FirstInstall", "")
        if (firstTime == "no") {
            navigateToMain()
        } else {
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.putString("FirstInstall", "no")
            editor.apply()
        }

        viewPager = view.findViewById(R.id.idViewPager)
        dotsLL = view.findViewById(R.id.idLLDots)
        presenter = SlidelViewModel(this)
        presenter.loadSliderData()
        skip = view.findViewById(R.id.idBtnSkip)

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

        return view
    }

    override fun showSlider(sliders: ArrayList<SliderModal>) {
        val adapter = SliderAdapter(requireContext(), sliders)
        viewPager.adapter = adapter
        addDots(sliders.size, 0)
    }

    override fun navigateToMain() {
        findNavController().navigate(R.id.action_introSliderFragment_to_authMainFragment)
    }

    private fun addDots(size: Int, pos: Int) {
        dots = Array(size) { TextView(context) }
        dotsLL.removeAllViews()
        dotsLL.gravity = Gravity.CENTER_HORIZONTAL
        for (i in 0 until size) {
            dots[i] = TextView(context)
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
