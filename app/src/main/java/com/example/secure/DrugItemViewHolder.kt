package com.example.secure

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.secure.databinding.RvDrugCardBinding

class DrugItemViewHolder(
    private val context: Context,
    private val binding: RvDrugCardBinding
): RecyclerView.ViewHolder(binding.root) {


    fun binDrugItem(drugItem: DrugItem){
        binding.name.text = drugItem.name
        binding.desc.text = drugItem.desc
    }
}