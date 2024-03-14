package com.example.secure

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.secure.databinding.FragmentTaskBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class TaskFragment : Fragment() {
    private lateinit var binding: FragmentTaskBinding
    private val viewModel: DataViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMMM, EEEE", Locale("ru"))
        val formattedDate = currentDate.format(formatter)
        binding.tvTaskFragDate.text = formattedDate

        viewModel.userName.observe(viewLifecycleOwner, Observer { name ->
            binding.tvTaskFragName.text = name
        })
        Glide.with(this).load(viewModel.userPhoto.value).into(binding.imageView).onLoadFailed(com.example.secure.R.drawable.img.toDrawable())

    }


}