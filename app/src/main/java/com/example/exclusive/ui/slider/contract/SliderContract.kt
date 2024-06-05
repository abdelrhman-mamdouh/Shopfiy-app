package com.example.exclusive.ui.slider.contract;


import com.example.exclusive.ui.slider.model.SliderModal

interface SliderContract {
    interface View {
        fun showSlider(sliders: ArrayList<SliderModal>)
        fun navigateToMain()
    }

    interface Presenter {
        fun loadSliderData()
        fun onSkipClicked()
    }
}
