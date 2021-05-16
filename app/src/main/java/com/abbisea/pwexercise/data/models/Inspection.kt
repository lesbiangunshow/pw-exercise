package com.abbisea.pwexercise.data.models

data class Inspection(
    val id: Int,
    val name: String,
    val questions: List<Question>,
    val possibleLocations: List<String>
)
