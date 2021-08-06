package com.ldnhat.demomaproute

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ldnhat.demomaproute.domain.Direction
import vn.map4d.types.MFLocationCoordinate

@SuppressLint("SetTextI18n")
@BindingAdapter("textDistance")
fun textDistance(textView: TextView, direction: Direction?){
    direction?.let {
        textView.text = it.directionResult.routes[0].distance.text
    }

}

@SuppressLint("SetTextI18n")
@BindingAdapter("textDuration")
fun textDuration(textView: TextView, direction: Direction?){
    direction?.let {
        textView.text = it.directionResult.routes[0].duration.text
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("textStartPosition")
fun textStartPosition(textView: TextView, mfLocationCoordinate: MFLocationCoordinate?){
    mfLocationCoordinate?.let {
        textView.text = "${it.latitude} , ${it.longitude}"
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("textEndPosition")
fun textEndPosition(textView: TextView, mfLocationCoordinate: MFLocationCoordinate?){
    mfLocationCoordinate?.let {
        textView.text = "${it.latitude} , ${it.longitude}"
    }
}
