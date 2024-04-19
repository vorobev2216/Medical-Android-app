package com.example.secure

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Query
import com.bumptech.glide.Glide
import com.example.secure.databinding.FragmentTaskBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class TaskFragment : Fragment(), DrugItemClickListener {
    private lateinit var binding: FragmentTaskBinding

    private val viewModel: DataViewModel by activityViewModels {
        DrugItemModelFactory((requireActivity().application as SecureApplication).repository)
    }
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

//        val nextDay = getNextDay()
//        val midnight = nextDay.timeInMillis
//
//        val savedMidnight = sharedPrefs.getLong("midnight", 0)
//        endTime = savedMidnight - System.currentTimeMillis()
//
//        countDownTimer = object : CountDownTimer(endTime, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
//                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
//                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
//                binding.tvQuestions.text = "Новый опрос через: ${String.format("%02d:%02d:%02d", hours, minutes, seconds)}"
//                binding.taskButton.setColorFilter(ContextCompat.getColor(context!!, R.color.grey_checked))
//            }
//            override fun onFinish() {
//                binding.taskButton.isEnabled = true
//                binding.tvQuestions.text = "Пройдите ежедневный опрос"
//                binding.taskButton.setColorFilter(ContextCompat.getColor(context!!, R.color.main_blue))
//
//            }
//        }
//
//        if (savedMidnight > System.currentTimeMillis()) {
//            binding.taskButton.isEnabled = false
//            countDownTimer.start()
//        } else {
//            binding.taskButton.isEnabled = true
//            binding.tvQuestions.text = "Пройдите ежедневный опрос"
//        }
//        binding.btnSimphtome.setOnClickListener{
//            parentFragmentManager.commit {
//                replace(R.id.TaskFrame, QuestionsFragment())
//                addToBackStack(null)
//            }
//        }

        binding.taskButton.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.TaskFrame, QuestionsFragment())
                addToBackStack(null)

            }

//            it.isEnabled = false
//            val nextDayMidnightCalc: Calendar = getNextDay()
//            sharedPrefs.edit().putLong("midnight", nextDayMidnightCalc.timeInMillis).apply()
//            val savedMidnight = sharedPrefs.getLong("midnight", 0)
//            endTime = savedMidnight - System.currentTimeMillis()
//            setMidnightAlarm()
//            val midnight = getNextDay().timeInMillis
//            sharedPrefs.edit().putLong("midnight", midnight).apply()
//            binding.taskButton.isEnabled = false
//            endTime = midnight - System.currentTimeMillis()
//            countDownTimer = object : CountDownTimer(endTime, 1000) {
//                override fun onTick(millisUntilFinished: Long) {
//                    val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
//                    val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
//                    val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
//
//                    binding.tvQuestions.setText("Следующий опрос через${String.format("%02d:%02d:%02d", hours, minutes, seconds)}")
//                }
//                override fun onFinish() {
//                    binding.taskButton.isEnabled = true
//
//                }
//
//            }.start()


        }

        if (viewModel.ratingHealth.value == null) {
            binding.progressCircular.isVisible = false
        } else {
            var progress = 0
            viewModel.ratingHealth.value?.let { el ->
                if (el.isNotEmpty()) {
                    progress = el.last()
                    updateProgress(progress)
                    binding.rating1.text = progress.toString()
                }
            }
            updateProgress(progress)
        }

        val db = Firebase.firestore
        val timestamp = com.google.firebase.Timestamp.now()
        val array = ArrayList<HashMap<String, Any>>() // или ArrayList<Any>()



        db.collection("Rating")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .addOnSuccessListener { result ->
                var data: HashMap<String, Any>? = null
                for (document in result) {
                    data = document.data as HashMap<String, Any>
                    array.add(data)
                }
                db.collection("Rating")
                val value1 = array[2]["val"]
                binding.progressCircular.progress = (value1 as Long).toInt()
                binding.rating1.text = (value1).toString()

                val value2 = array[1]["val"]
                binding.rating2.text = value2.toString()
                binding.progressCircular2.progress = (value2 as Long).toInt()

                val value3 = array[0]["val"]
                Log.d("RRR","$value3")
                binding.rating3.text = value3.toString()
                binding.progressCircular3.progress = (value3 as Long).toInt()


                val timestamp3 = array[2]["timestamp"] as com.google.firebase.Timestamp
                val timestamp2 = array[1]["timestamp"] as com.google.firebase.Timestamp
                val timestamp1 = array[0]["timestamp"] as com.google.firebase.Timestamp

                val date1 = timestamp1.toDate()
                val date2 = timestamp2.toDate()
                val date3 = timestamp3.toDate()

                val sdf = SimpleDateFormat("d MMM", Locale("ru"))

                val formattedDate1 = sdf.format(date1)
                val formattedDate2 = sdf.format(date2)
                val formattedDate3 = sdf.format(date3)
                binding.tvTime1.text = formattedDate3
                binding.tvTime2.text = formattedDate2
                binding.tvTime3.text = formattedDate1
            }






    }
    private fun convertMillisToTime(){

    }
    private fun setMidnightAlarm() {
        val nextDay = getNextDay()
        val midnight = nextDay.timeInMillis


        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(requireActivity(), MidnightReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                requireActivity(), 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, midnight, alarmIntent)
    }

    private fun updateProgress(progress: Int) {
        val progressBar = binding.progressCircular
        progressBar.progress = progress
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