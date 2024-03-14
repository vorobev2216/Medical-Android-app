package com.example.secure

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.secure.databinding.FragmentAddDrugBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddDrugFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddDrugBinding
    val viewModel: DataViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddDrugBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addButton.setOnClickListener {
            val drugName = binding.edDrugName.text.toString()
            viewModel.setDrugName(drugName)
            binding.edDrugName.setText("")
            dismiss()
        }


    }

}