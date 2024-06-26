package com.example.secure

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.secure.Fragments.AiChatFragment
import com.example.secure.Fragments.ProfileFragment
import com.example.secure.Fragments.TaskFragment
import com.example.secure.Room.SecureApplication
import com.example.secure.ViewModel.DataViewModel
import com.example.secure.ViewModel.DrugItemModelFactory
import com.example.secure.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: DataViewModel by viewModels{ DrugItemModelFactory((application as SecureApplication).repository) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentChanger(TaskFragment())

        val username = intent.getStringExtra("username").toString()
        val email = intent.getStringExtra("email").toString()
        val number = intent.getStringExtra("number").toString()
        val photo = intent.getStringExtra("photo").toString()
        viewModel.setUserName(username)
        viewModel.setUserEmail(email)
        viewModel.setUserNumber(number)
        viewModel.setUserPhoto(photo)
        Log.d("RRR",viewModel.userPhoto.value.toString())


        binding.bottomNavigationView.setOnNavigationItemSelectedListener{

            when (it.itemId) {
                R.id.Profile -> fragmentChanger(ProfileFragment())
                R.id.Task -> fragmentChanger(TaskFragment())
                R.id.Chat -> fragmentChanger(AiChatFragment())
            }
            true
        }

    }


    private fun fragmentChanger(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
            .commit()
    }

    private fun logOut() {
        val ab = supportActionBar
        actionBar!!.title = "Secure"
    }


}

