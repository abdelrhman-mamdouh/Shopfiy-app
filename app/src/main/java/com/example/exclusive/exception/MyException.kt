package com.example.exclusive.exception

val userErrors: List<MyException>? = null
data class MyException (val field: List<String>?,
                        val message: String?)
