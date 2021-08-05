package com.ldnhat.demomaproute

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ldnhat.demomaproute.domain.Direction

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