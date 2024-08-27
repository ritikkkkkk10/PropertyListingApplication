package com.ritikprajapati.propertylistingapplication

// PropertyAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PropertyAdapter(private val properties: List<Property>) :
    RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    class PropertyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val propertyName: TextView = view.findViewById(R.id.propertyName)
        val location: TextView = view.findViewById(R.id.location)
        val price: TextView = view.findViewById(R.id.price)
        val shortDescription: TextView = view.findViewById(R.id.shortDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_property, parent, false)
        return PropertyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]
        holder.propertyName.text = property.propertyName
        holder.location.text = property.location
        holder.price.text = property.price
        holder.shortDescription.text = property.shortDescription
    }

    override fun getItemCount() = properties.size
}
