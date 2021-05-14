package com.abbisea.pwexercise.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(resource: Int, attachToRoot: Boolean = false): View {
    val inflater = LayoutInflater.from(this.context)
    return inflater.inflate(resource, this, attachToRoot)
}