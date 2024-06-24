package com.example.exclusive.ui.slider.model

import com.example.exclusive.R
import com.example.exclusive.ui.slider.contract.SliderContract

class SlidelViewModel(private val view: SliderContract.View) : SliderContract.Presenter {

    override fun loadSliderData() {
        val sliderModalArrayList = ArrayList<SliderModal>()
        sliderModalArrayList.add(
            SliderModal(
                "What will I find today at Exclusive?",
                "Never stress about finding products again!",
                "animation.json",
                R.drawable.gradient_three
            )
        )
        sliderModalArrayList.add(
            SliderModal(
                "Exclusive Shopping Experience",
                "Browse through categories, discover new products, and explore a wide range of options",
                "loader.json",
                R.drawable.gradient_two
            )
        )
        sliderModalArrayList.add(
            SliderModal(
                "App Features",
                "Enjoy easy shopping, Exclusive also offers recommendations and a wishlist feature",
                "offer.json",
                R.drawable.wave2
            )
        )
        view.showSlider(sliderModalArrayList)
    }


    override fun onSkipClicked() {
        view.navigateToMain()
    }
}
