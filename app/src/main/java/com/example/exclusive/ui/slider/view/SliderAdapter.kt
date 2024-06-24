package com.example.exclusive.ui.slider.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.airbnb.lottie.LottieAnimationView
import com.example.exclusive.R
import com.example.exclusive.ui.slider.model.SliderModal

class SliderAdapter(
    private val context: Context,
    private val sliderModalArrayList: ArrayList<SliderModal>
) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    override fun getCount(): Int {
        return sliderModalArrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater!!.inflate(R.layout.slider_layout, container, false)
        val animationView: LottieAnimationView = view.findViewById(R.id.animationView)
        val titleTV: TextView = view.findViewById(R.id.idTVtitle)
        val headingTV: TextView = view.findViewById(R.id.idTVheading)
        val sliderRL: RelativeLayout = view.findViewById(R.id.idRLSlider)
        val modal: SliderModal = sliderModalArrayList[position]
        titleTV.text = modal.title
        headingTV.text = modal.heading
        animationView.setAnimation(modal.animationFileName)
        sliderRL.setBackgroundResource(modal.backgroundDrawable)
        animationView.loop(true)
        animationView.playAnimation()
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}
