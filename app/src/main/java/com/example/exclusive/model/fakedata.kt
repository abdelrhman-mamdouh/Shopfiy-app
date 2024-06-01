package com.example.exclusive.model

data class Review (val name: String, val rating: Double, val comment: String)
val fakeReviews = listOf(
    Review("John Doe", 4.5, "Great product!"),
    Review("Jane Smith", 3.0, "Okay product"),
    Review("Bob Johnson", 5.0, "Amazing product!"),
    Review("Sarah Lee", 2.5, "Disappointing product"),
    Review("Michael Selgado", 4.0, "Good product"),
    Review("Cress Davis", 3.5, "Decent product"),
    Review("Marcos Wilson", 5.0, "Awesome product!"),
    Review("Olivia Taylor", 2.0, "Could be better"),
    Review("Bokayo Anderson", 4.5, "Good value for money"),
    Review("Sophia Nguyen", 3.5, "Decent product"),
    Review("William Johnson", 4.0, "Good product"),
    Review("Emma Davis", 3.0, "Could be better"),
    Review("Michael Chen", 4.5, "Good product"),
    Review("Emily David", 3.5, "Decent product"),
    Review("Hary Wilson", 5.0, "Awesome product!"),
    Review("Olivia Rony", 2.0, "Could be better"),
    Review("James Anderson", 4.5, "Good value for money"),
    Review("Sophia Nguyen", 3.5, "Decent product"),
    Review("Gabriel Mosess", 4.0, "Good product"),
    Review("Emma Davis", 3.0, "Could be better"),
    Review("Michael Almeida", 4.5, "Good product"),
    Review("Anna Davis", 3.5, "Decent product"),
    Review("David Wilson", 5.0, "Awesome product!"),
    Review("Javier Taylor", 2.0, "Could be better"),
    Review("James Rice", 4.5, "Good value for money"),
    Review("Raul Nguyen", 3.5, "Decent product"),
    Review("William Saliba", 4.0, "Good product"),
    Review("Edward Alfonso", 3.0, "Could be better"),

)
fun getRandomThreeReviews(): List<Review> {
    return fakeReviews.shuffled().take(3)
}
fun getRandomNineReviews(): List<Review> {
    return fakeReviews.shuffled().take(9)
}