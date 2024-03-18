package com.example.secure

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.secure.databinding.RvDrugCardBinding
import com.google.android.material.timepicker.TimeFormat
import java.time.format.DateTimeFormatter

class DrugItemViewHolder(
    private val context: Context,
    private val binding: RvDrugCardBinding,
    private val clickListener: DrugItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    private var idx = -1

    fun binDrugItem(drugItem: DrugItem) {
        binding.name.text = drugItem.name
        binding.desc.text = drugItem.desc

        if (drugItem.isCompleted()) {
            binding.radioButton.setBackgroundColor(Color.TRANSPARENT)
            binding.root.setCardBackgroundColor(Color.parseColor("#F9F9FF"))
            binding.name.setTextColor(Color.parseColor("#99010101"))
            binding.desc.setTextColor(Color.parseColor("#99010101"))
            binding.ivClock.setColorFilter(
                ContextCompat.getColor(context, R.color.main_blue),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            binding.ivBell.setColorFilter(
                ContextCompat.getColor(context, R.color.main_blue),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            binding.radioButton.setImageResource(drugItem.imageResource())
            binding.radioButton.setColorFilter(
                ContextCompat.getColor(context, R.color.main_blue),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            binding.duetime.setTextColor(Color.parseColor("#99010101"))


        } else {
            binding.radioButton.setBackgroundColor(Color.TRANSPARENT)
            binding.root.setCardBackgroundColor(Color.parseColor("#313181"))
            binding.radioButton.setColorFilter(
                ContextCompat.getColor(context, R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

        binding.radioButton.setOnClickListener {
            clickListener.completeDrugItem(drugItem)
        }

        binding.drugcard.setOnClickListener {
            clickListener.editDrugItem(drugItem)
        }

        if (drugItem.dueTime != null) {
            binding.duetime.text = timeFormat.format(drugItem.dueTime)
        } else {
            binding.duetime.text = ""
        }
    }


//    binding.root.setCardBackgroundColor(Color.parseColor("#313181"))
//    binding.radioButton.setOnCheckedChangeListener { _, isChecked ->
//        if(isChecked){
//            binding.root.setCardBackgroundColor(Color.parseColor("#F9F9FF"))
//            binding.name.setTextColor(Color.parseColor("#99010101"))
//            binding.desc.setTextColor(Color.parseColor("#99010101"))
//        } else{
//            binding.root.setCardBackgroundColor(Color.parseColor("#313181"))
//        }
//    }
}