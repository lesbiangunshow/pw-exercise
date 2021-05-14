package com.abbisea.pwexercise.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(
    val displayText: String,
    val answers: List<String>
) : Parcelable
