package com.ritikprajapati.propertylistingapplication

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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

//    fun updateData(newList: List<Property>) {
//        propertyList.clear()
//        propertyList.addAll(newList)
//        notifyDataSetChanged()
//    }

    inner class PropertyViewHolder(private val binding: ItemPropertyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property) {
            binding.propertyName.text = property.propertyName
            binding.location.text = property.location
            binding.price.text = property.price
            binding.shortDescription.text = property.shortDescription

            itemView.setOnClickListener {
                Log.d("PropertyAdapter", "Item clicked: ${property.propertyName}")
                Toast.makeText(itemView.context, "Clicked on ${property.propertyName}", Toast.LENGTH_SHORT).show()

                val context = it.context
                val intent = Intent(context, PropertyDetailActivity::class.java).apply {
                    putExtra("propertyName", property.propertyName)
                    putExtra("location", property.location)
                    putExtra("price", property.price)
                    putExtra("shortDescription", property.shortDescription)
                    putExtra("longDescription", property.longDescription)
                    putExtra("contactPhone", property.contactPhone)
                    putExtra("contactEmail", property.contactEmail)
                    putExtra("imageUrls", ArrayList(property.imageUrls))
                }
                context.startActivity(intent)
            }
        }
    }
}
