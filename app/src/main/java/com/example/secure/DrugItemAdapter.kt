package com.example.secure

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secure.databinding.RvDrugCardBinding

class DrugItemAdapter(
    private val drugItems: List<DrugItem>
) : RecyclerView.Adapter<DrugItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = RvDrugCardBinding.inflate(from, parent, false)
        return  DrugItemViewHolder(parent.context,binding)

    }

    override fun getItemCount(): Int {
        return drugItems.size
    }

    override fun onBindViewHolder(holder: DrugItemViewHolder, position: Int) {
        holder.binDrugItem(drugItems[position])
    }
}