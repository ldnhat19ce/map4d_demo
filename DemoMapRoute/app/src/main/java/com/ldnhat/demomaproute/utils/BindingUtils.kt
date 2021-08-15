package com.ldnhat.demomaproute

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ldnhat.demomaproute.domain.Direction
import com.ldnhat.demomaproute.domain.PlaceDetail
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

@SuppressLint("SetTextI18n")
@BindingAdapter("textMode")
fun modeMapText(button: Button, is3DMode : Boolean?){
    is3DMode?.let {
        if (it){
            button.text = "2D"
        }else{
            button.text = "3D"
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("placeImage")
fun bottomSheetPlaceImage(imageView: ImageView, placeDetail: PlaceDetail?){
    placeDetail?.let {
        if (it.result.photos.isNotEmpty()){
            Glide.with(imageView)
                .load(it.result.photos[0].url)
                .into(imageView)
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("textPlaceName")
fun bottomSheetPlaceName(textView: TextView, placeDetail: PlaceDetail?){
    placeDetail?.let {
        textView.text = it.result.name
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("textPlaceAddress")
fun bottomSheetPlaceAddress(textView: TextView, placeDetail: PlaceDetail?){
    placeDetail?.let {
        textView.text = it.result.address
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("textPlaceLocation")
fun bottomSheetPlaceLocation(textView: TextView, placeDetail: PlaceDetail?){
    placeDetail?.let {
        textView.text = it.result.location.lat.toString() + ", "+it.result.location.lng.toString()
    }
}
