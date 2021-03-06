package com.example.koombeatest.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.ViewGroup
import jp.wasabeef.blurry.Blurry
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun isConnectedToInternet(context: Context) : Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
    else {
        if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
            return true
        }
    }
    return false
}

fun shortDate(date: String) : String{
    Timber.d("Date: %s", date)
    try {
        val d = date.split(" ").slice(listOf(1, 2, 3))
        return d.joinToString(" ")
    } catch (e: Exception){}
    return date
}

fun addBlurEffect(view: View?) {
    Blurry.with(view?.context).radius(25).sampling(2).onto(view as ViewGroup?)
}

fun removeBlurEffect(view: View?) {
    Blurry.delete(view as ViewGroup)
}