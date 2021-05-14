package com.abbisea.pwexercise.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Inspection(
    val id: Int,
    val name: String,
    val questions: List<Question>,
    val possibleLocations: List<String>
): Parcelable {

}