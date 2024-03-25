package com.example.secure

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.secure.databinding.FragmentQuestionsBinding


class QuestionsFragment : Fragment() {
    private lateinit var binding: FragmentQuestionsBinding
    private val viewModel: DataViewModel by activityViewModels()


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
        binding.endBtn.setOnClickListener {
            calculateRating()
            if (viewModel.ratingHealth.value!! < 50)
                alertWindow()
        }
    }

    private fun alertWindow() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setView(layoutInflater.inflate(R.layout.dialog_callambulance, null))
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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

        val shuffledValues = questions.shuffled()

        for (i in textViews.indices) {
            textViews[i].text = shuffledValues[i]
        }
    }

    private fun calculateRating() {
        val radioBtn1 = listOf<RadioButton>(
            binding.answer11,
            binding.answer21,
            binding.answer31,
            binding.answer41,
            binding.answer51,
            binding.answer61,
            binding.answer71,
            binding.answer81,
            binding.answer91,
            binding.answer101
        )
        val radioBtn2 = listOf<RadioButton>(
            binding.answer12,
            binding.answer22,
            binding.answer32,
            binding.answer42,
            binding.answer52,
            binding.answer62,
            binding.answer72,
            binding.answer82,
            binding.answer92,
            binding.answer102
        )
        val radioBtn3 = listOf<RadioButton>(
            binding.answer13,
            binding.answer23,
            binding.answer33,
            binding.answer43,
            binding.answer53,
            binding.answer63,
            binding.answer73,
            binding.answer83,
            binding.answer93,
            binding.answer103
        )
        val radioBtn4 = listOf<RadioButton>(
            binding.answer14,
            binding.answer24,
            binding.answer34,
            binding.answer44,
            binding.answer54,
            binding.answer64,
            binding.answer74,
            binding.answer84,
            binding.answer94,
            binding.answer104
        )

        var sumRating = 0

        for (btn1 in radioBtn1) {
            if (btn1.isChecked) {
                sumRating += 100
            }
        }
        for (btn2 in radioBtn2) {
            if (btn2.isChecked) {
                sumRating += 70
            }
        }
        for (btn3 in radioBtn3) {
            if (btn3.isChecked) {
                sumRating += 40
            }
        }
        for (btn4 in radioBtn4) {
            if (btn4.isChecked) {
                sumRating += 0
            }
        }

        val finalRating = (sumRating / 10) * 100 / 100

        viewModel.setRating(finalRating)


    }
}