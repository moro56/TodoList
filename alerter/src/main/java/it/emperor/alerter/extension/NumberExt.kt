package it.emperor.alerter.extension

import android.content.res.Resources

fun Float.toPx(): Float = this * Resources.getSystem().displayMetrics.density
