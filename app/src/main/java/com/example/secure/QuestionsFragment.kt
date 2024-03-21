package com.example.secure

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.secure.databinding.FragmentQuestionsBinding
import java.util.Random


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

        val textViews = listOf<TextView>(
            binding.questionText1,
            binding.questionText2,
            binding.questionText3,
            binding.questionText4,
            binding.questionText5,
            binding.questionText6,
            binding.questionText7,
            binding.questionText8,
            binding.questionText9,
            binding.questionText10
        )

        val questions: ArrayList<String> = arrayListOf(
            "da",
            "net",
            "mb",
            "fff",
            "qed",
            "mmm",
            "12",
            "34",
            "45",
            "67",
            "89",
            "0",
            "-"
        )

        val shuffeldValues = questions.shuffled()

        for (i in textViews.indices) {
            textViews[i].text = shuffeldValues[i]

        }


    }
}