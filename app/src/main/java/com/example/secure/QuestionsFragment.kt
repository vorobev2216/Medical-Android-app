package com.example.secure

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.replace
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.secure.databinding.DialogCallambulanceBinding
import com.example.secure.databinding.FragmentQuestionsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


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
        val db = Firebase.firestore
        binding.endBtn.setOnClickListener {
            if (checkQuiz()) {
                calculateRating()
                var lastEl = 0
                val timestamp = com.google.firebase.Timestamp.now()
                viewModel.ratingHealth.value?.let { list ->
                    if (list.isNotEmpty()) {
                        lastEl = list.last()
                    }
                }
                var value = hashMapOf("val" to lastEl,
                    "timestamp" to timestamp )
                db.collection("Rating").add(value)
                if (lastEl < 50) {
                    createPhoneDialog()
                    db.collection("Rating").add(value)
                }
                Toast.makeText(
                    context,
                    "Спасибо за ответ! Увидимся завтра!",
                    Toast.LENGTH_SHORT
                ).show()
                parentFragmentManager.commit {
                    replace(R.id.QuestionsFrame, TaskFragment())
                }


            }
        }

    }

    private fun createPhoneDialog() {
        val alertBinding = DialogCallambulanceBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )

        val builder = AlertDialog.Builder(context).apply {
            setView(alertBinding.root)
        }

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertBinding.button.setOnClickListener {
            val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${900}")
            }
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    1
                )
            } else {
                startActivity(phoneIntent)
                parentFragmentManager.commit {
                    replace(R.id.QuestionsFrame, TaskFragment())
                }
                dialog.dismiss()
            }
        }
        alertBinding.button2.setOnClickListener {
            dialog.dismiss()
            parentFragmentManager.commit {
                replace(R.id.QuestionsFrame, TaskFragment())
            }

        }

    }

    private fun checkQuiz(): Boolean {
        val radioGroup: ArrayList<RadioGroup> = arrayListOf(
            binding.radioGroup1,
            binding.radioGroup2,
            binding.radioGroup3,
            binding.radioGroup4,
            binding.radioGroup5,
            binding.radioGroup6,
            binding.radioGroup7,
            binding.radioGroup8,
            binding.radioGroup9,
            binding.radioGroup10
        )
        for (rg in radioGroup) {
            if (rg.checkedRadioButtonId == -1) {
                val snackbar = Snackbar.make(requireView(), "", Snackbar.LENGTH_LONG)
                val layout = snackbar.view as Snackbar.SnackbarLayout

                val snackView = layoutInflater.inflate(R.layout.custom_snackbar, null)

                layout.setPadding(0, 0, 0, 0)
                layout.addView(snackView, 0)


                val snackbarText = snackView.findViewById<TextView>(R.id.snackbar_text)

                layout.background =
                    ContextCompat.getDrawable(context!!, R.drawable.background_white)


                snackbarText.text = "Ответьте на все вопросы!"

                snackbar.show()

                return false
            }
        }
        return true
    }

    private fun alertWindow() {

        val alertBinding = DialogCallambulanceBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(context).apply {

            setView(alertBinding.root)
        }

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
            "У Вас была какая-либо СЫПЬ?",
            "Были ли у Вас ПРОЛЕЖНИ?",
            "Насколько Сил был ОЖОГ КОЖИ ПОСЛЕ ОБЛУЧЕНИЯ?",
            "Вы замечали ПОКАЛЫВАНИЕВ КИСТЯХ ИЛИ СТОПАХ?",
            "Насколько ВЫРАЖЕННОЙ была БОЛЬ В СУСТАВАХ?",
            "Насколько ВЫРАЖЕННОЙ была УТОМЛЯЕМОСТЬ?",
            "Насколько ВЫРАЖЕННЫМ было СНИЖЕНИЕ ЛИБИДО",
            "Насколько ВЫРАЖЕННЫМИ были ДРОЖЬ ИЛИ ОЗНОБ?",
            "Насколько ВЫРАЖЕННОЙ была СУХОСТЬ ВО РТУ?",
            "Насколько ВЫРАЖЕННЫМИ были ТРУДНОСТИ ПРИ ГЛОТАНИИ?",
            "Насколько ВЫРАЖЕННЫМИ были ЯЗВЫ НА СЛИЗИСТОЙ РТА?",
            "Насколько ВЫРАЖЕННЫМ было ВЗДУТИЕ ЖИВОТА?",
            "Насколько ВЫРАЖЕННОЙ была САМАЯ СИЛЬНАЯ ИКОТА?"
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