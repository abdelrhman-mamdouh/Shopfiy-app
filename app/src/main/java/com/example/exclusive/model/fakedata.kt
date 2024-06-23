package com.example.exclusive.model

data class Review (val name: String, val rating: Double, val comment: String)
val fakeReviews = listOf(
    Review("Ali Ahmed", 4.5, "Great product!"),
    Review("Mohamed Gomaa", 3.0, "Okay product"),
    Review("Nour Sayed", 5.0, "Amazing product!"),
    Review("Sara Nagy", 2.5, "Disappointing product"),
    Review("Kareem Gorage", 4.0, "Good product"),
    Review("Daila Wael", 3.5, "Decent product"),
    Review("Mahmoud Imam", 5.0, "Awesome product!"),
    Review("Noha Ibrahim", 2.0, "Could be better"),
    Review("Ola Tarek", 4.5, "Good value for money"),
    Review("Hassan Alaam", 3.5, "Decent product"),
    Review("Mona Alaa", 4.0, "Good product"),
    Review("Ahmed Alaa", 3.0, "Could be better"),
    Review("Marwan Mohamed", 4.5, "Good product"),
    Review("Heba Ismail", 3.5, "Decent product"),
    Review("Ahmed Mazen", 5.0, "Awesome product!"),
    Review("Mohamed Galal", 2.0, "Could be better"),
    Review("Hagar Alaa", 4.5, "Good value for money"),
    Review("Beshoy Adel", 3.5, "Decent product"),
    Review("Esraa Hassan", 4.0, "Good product"),
    Review("Manal Hussein", 3.0, "Could be better"),
    Review("Omar Mohamed", 4.5, "Good product"),
    Review("Raghad Alaa", 3.5, "Decent product"),
    Review("Aamir Naseer", 5.0, "Awesome product!"),
    Review("Yasser Hamed", 2.0, "Could be better"),
    Review("Yomana elmahdy", 4.5, "Good value for money"),
    Review("Mousa Issa", 3.5, "Decent product"),


)
fun getRandomThreeReviews(): List<Review> {
    return fakeReviews.shuffled().take(3)
}
fun getRandomNineReviews(): List<Review> {
    return fakeReviews.shuffled().take(9)
}