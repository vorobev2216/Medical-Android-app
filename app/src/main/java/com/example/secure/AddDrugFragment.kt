package com.example.secure

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.secure.databinding.FragmentAddDrugBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddDrugFragment(var drugItem: DrugItem?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddDrugBinding
    val viewModel: DataViewModel by activityViewModels()


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
        }

        binding.addButton.setOnClickListener {
            val drugName = binding.edDrugName.text.toString()
            val drugDesc = binding.edDrugDesc.text.toString()
            if(drugItem == null){
                val newDrug = DrugItem(drugName,drugDesc,null,null)
                viewModel.addDrugItem(newDrug)
            }
            else{
                viewModel.updateDrugItem(drugItem!!.id,drugName,drugDesc,null)
            }
            binding.edDrugName.setText("")
            binding.edDrugDesc.setText("")
            dismiss()
        }


    }

}