package com.example.exclusive.data.model

data class Currencies(
    val base: String,
    val ms: Int,
    val results: Map<String, Double>,
    val updated: String
)