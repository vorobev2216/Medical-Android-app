package com.example.secure

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.secure.databinding.FragmentAddDrugBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime

class AddDrugFragment(var drugItem: DrugItem?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddDrugBinding
    val viewModel: DataViewModel by activityViewModels()
    private var dueTime: LocalTime? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddDrugBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (drugItem == null) {
            binding.tvAddDrug.text = "Добавьте препарат"
        } else {
            binding.tvAddDrug.text = "Внесите изменения"
            var editable = Editable.Factory.getInstance()
            binding.edDrugName.text = editable.newEditable(drugItem!!.name)
            binding.edDrugDesc.text = editable.newEditable(drugItem!!.desc)
            if(drugItem!!.dueTime == null){
                dueTime = drugItem!!.dueTime!!
                updateButtonText()
            }
        }

        binding.addButton.setOnClickListener {
            val drugName = binding.edDrugName.text.toString()
            val drugDesc = binding.edDrugDesc.text.toString()
            if (drugItem == null) {
                val newDrug = DrugItem(drugName, drugDesc, dueTime, null)
                viewModel.addDrugItem(newDrug)
            } else {
                viewModel.updateDrugItem(drugItem!!.id, drugName, drugDesc, dueTime)
            }
            binding.edDrugName.setText("")
            binding.edDrugDesc.setText("")
            dismiss()
        }
        binding.setTimer.setOnClickListener {
            openTimePicker()
        }


    }

    private fun openTimePicker() {
        if(dueTime == null){
            dueTime = LocalTime.now()
        }
        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Время приема")
        dialog.show()
    }

    private fun updateButtonText() {
        binding.setTimer.text = String.format("%02d:%02d",dueTime!!.hour,dueTime!!.minute)
    }

}