package com.example.secure

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.secure.databinding.FragmentTaskBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


class TaskFragment : Fragment(), DrugItemClickListener {
    private lateinit var binding: FragmentTaskBinding
    private val viewModel: DataViewModel by activityViewModels()
    private val sharedPrefs by lazy {
        requireActivity().getSharedPreferences("button", AppCompatActivity.MODE_PRIVATE)
    }

    lateinit var countDownTimer: CountDownTimer
    var endTime: Long = 0

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


        binding.addDrug.setOnClickListener {
            AddDrugFragment(null).show(childFragmentManager, "AddDrug")
        }



        viewModel.userName.observe(viewLifecycleOwner, Observer { name ->
            binding.tvTaskFragName.text = name
        })
        Glide.with(this).load(viewModel.userPhoto.value).into(binding.imageView)
            .onLoadFailed(com.example.secure.R.drawable.img.toDrawable())

        setRecyclerView()

        val nextDay = getNextDay()
        val midnight = nextDay.timeInMillis

        val savedMidnight = sharedPrefs.getLong("midnight", 0)
        endTime = savedMidnight - System.currentTimeMillis()

        countDownTimer = object : CountDownTimer(endTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                binding.tvQuestions.text = "Новый опрос через: ${String.format("%02d:%02d:%02d", hours, minutes, seconds)}"
                binding.taskButton.setColorFilter(ContextCompat.getColor(context!!, R.color.grey_checked))
            }
            override fun onFinish() {
                binding.taskButton.isEnabled = true
                binding.tvQuestions.text = "Пройдите ежедневный опрос"
                binding.taskButton.setColorFilter(ContextCompat.getColor(context!!, R.color.main_blue))

            }
        }

        if (savedMidnight > System.currentTimeMillis()) {
            binding.taskButton.isEnabled = false
            countDownTimer.start()
        } else {
            binding.taskButton.isEnabled = true
            binding.tvQuestions.text = "Пройдите ежедневный опрос"
        }


        binding.taskButton.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.TaskFrame, QuestionsFragment())
                addToBackStack(null)
            }

            it.isEnabled = false
            val nextDayMidnightCalc: Calendar = getNextDay()
            sharedPrefs.edit().putLong("midnight", nextDayMidnightCalc.timeInMillis).apply()

            setMidnightAlarm()
            val midnight = getNextDay().timeInMillis
            sharedPrefs.edit().putLong("midnight", midnight).apply()
            binding.taskButton.isEnabled = false
            endTime = midnight - System.currentTimeMillis()
            countDownTimer = object : CountDownTimer(endTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

                    binding.tvQuestions.setText("Следующий опрос через${String.format("%02d:%02d:%02d", hours, minutes, seconds)}")
                }
                override fun onFinish() {
                    binding.taskButton.isEnabled = true

                }
            }.start()

            if(binding.taskButton.isEnabled){

            }


        }


    }

    private fun setMidnightAlarm() {
        val nextDay = getNextDay()
        val midnight = nextDay.timeInMillis




        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(requireActivity(),MidnightReceiver::class.java).let {
            intent ->
            PendingIntent.getBroadcast(requireActivity(),0,intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,midnight,alarmIntent)
    }

    private fun getNextDay(): Calendar {
        return Calendar.getInstance().apply {
            add(Calendar.DATE, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
    }

    private fun setRecyclerView() {
        viewModel.drugItems.observe(viewLifecycleOwner) {
            binding.rvDrug.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = DrugItemAdapter(it!!, this@TaskFragment)
            }
        }
    }

    override fun editDrugItem(drugItem: DrugItem) {
        AddDrugFragment(drugItem).show(childFragmentManager, "AddDrug")
    }

    override fun completeDrugItem(drugItem: DrugItem) {
        viewModel.setCompleted(drugItem)
    }


}