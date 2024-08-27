package com.ritikprajapati.propertylistingapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ritikprajapati.propertylistingapplication.databinding.ItemPropertyBinding

class PropertyAdapter(private val propertyList: List<Property>) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val binding = ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = propertyList[position]
        holder.bind(property)
    }

    override fun getItemCount() = propertyList.size

    inner class PropertyViewHolder(private val binding: ItemPropertyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property) {
            binding.propertyName.text = property.propertyName
            binding.location.text = property.location
            binding.price.text = property.price
            binding.shortDescription.text = property.shortDescription

            // Optionally, handle click events to show detailed view
            itemView.setOnClickListener {
                // Handle click to show detailed view of the property
            }
        }
    }
}
