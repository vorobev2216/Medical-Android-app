package com.example.secure.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.secure.ViewModel.DataViewModel
import com.example.secure.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: DataViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {
        auth = Firebase.auth
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.tvUserName.text = userName
        }

        viewModel.userEmail.observe(viewLifecycleOwner) { email ->
            binding.tvMail.text = email
        }

        viewModel.userNumber.observe(viewLifecycleOwner) { number ->
            binding.tvPhoneNumber.text = number
        }

        Glide.with(this).load(viewModel.userPhoto.value).into(binding.ivUserPhoto).onLoadFailed(com.example.secure.R.drawable.img.toDrawable())





        binding.btnLogout.setOnClickListener {
            auth.signOut()
            activity?.finish()

        }

    }


}