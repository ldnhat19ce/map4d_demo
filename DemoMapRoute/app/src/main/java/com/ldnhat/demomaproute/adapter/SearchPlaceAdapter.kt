package com.ldnhat.demomaproute.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ldnhat.demomaproute.databinding.ItemSearchPlaceBinding
import com.ldnhat.demomaproute.domain.Location
import com.ldnhat.demomaproute.domain.Result

class SearchPlaceAdapter(val clickListener : ClickListener) :
    ListAdapter<Result, SearchPlaceAdapter.SearchPlaceViewHolder>(ResultDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPlaceViewHolder {

        return SearchPlaceViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchPlaceViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class SearchPlaceViewHolder(private val placeBinding: ItemSearchPlaceBinding) : RecyclerView.ViewHolder(placeBinding.root){

        fun bind(item : Result, clickListener: ClickListener){
            placeBinding.place = item
            placeBinding.clickListener = clickListener
            placeBinding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : SearchPlaceViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchPlaceBinding.inflate(layoutInflater, parent, false)

                return SearchPlaceViewHolder(binding)
            }
        }
    }

    class ResultDiffCallback : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }


}

class ClickListener(val clickListener : (location : Location) -> Unit){
    fun onclick(data : Result) = clickListener(data.location)
}