@file:JvmName("ExtensionsUtils")

package com.example.fella.foursquare.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fella.foursquare.R
import com.facebook.drawee.view.SimpleDraweeView


fun Context.isNetworkAvailable(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo: NetworkInfo? = cm.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun SimpleDraweeView.loadImg(imageUrl: String?) {
    if(imageUrl!=null&&!TextUtils.isEmpty(imageUrl))
        this.setImageURI(imageUrl)
}


