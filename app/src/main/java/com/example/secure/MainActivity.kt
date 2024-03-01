package com.example.secure

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.secure.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentChanger(TaskFragment())

        binding.bottomNavigationView.setOnItemReselectedListener {

            when(it.itemId){
                R.id.Profile -> fragmentChanger(ProfileFragment())
                R.id.Task -> fragmentChanger(TaskFragment())
                R.id.Chat -> fragmentChanger(AiChatFragment())
            }

        }

    }

    private fun fragmentChanger(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }


}

