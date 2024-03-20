package com.example.secure

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.secure.databinding.FragmentQuestionsBinding


class QuestionsFragment : Fragment() {
    lateinit var binding: FragmentQuestionsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillQuestions()
    }

    private fun fillQuestions() {

        val textViews = listOf<TextView>(binding.questionText1, binding.questionText2)
        val questions: ArrayList<String> = arrayListOf(
            "da",
            "net",
            "mb"
        )
        val remainingQuestions: ArrayList<String> = questions



        for (tv in textViews) {
            val rndmIdx = (0..remainingQuestions.size).random()
            tv.text = questions[rndmIdx]
            remainingQuestions.removeAt(rndmIdx)
        }


    }
}