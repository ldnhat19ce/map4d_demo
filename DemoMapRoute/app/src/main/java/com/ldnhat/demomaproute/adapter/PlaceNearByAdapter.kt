package com.ldnhat.demomaproute.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ldnhat.demomaproute.databinding.PlaceNearItemViewBinding
import com.ldnhat.demomaproute.domain.PlaceNearByResult

class PlaceNearByAdapter : ListAdapter<PlaceNearByResult, PlaceNearByAdapter.PlaceNearByViewHolder>(ResultDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceNearByViewHolder {

        return PlaceNearByViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlaceNearByViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    class PlaceNearByViewHolder(private val placeNearBinding : PlaceNearItemViewBinding) :
        RecyclerView.ViewHolder(placeNearBinding.root){

        fun bind(placeNearByResult: PlaceNearByResult){
            placeNearBinding.placeNearBy = placeNearByResult
            placeNearBinding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : PlaceNearByViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PlaceNearItemViewBinding.inflate(layoutInflater, parent, false)

                return PlaceNearByViewHolder(binding)
            }
        }
    }

    class ResultDiffUtil : DiffUtil.ItemCallback<PlaceNearByResult>(){
        override fun areItemsTheSame(
            oldItem: PlaceNearByResult,
            newItem: PlaceNearByResult
        ): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PlaceNearByResult,
            newItem: PlaceNearByResult
        ): Boolean {
            return oldItem == newItem
        }
    }
}

