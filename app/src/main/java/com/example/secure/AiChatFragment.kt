package com.example.secure

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.secure.databinding.FragmentAiChatBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AiChatFragment : Fragment() {
    private val viewModel: DataViewModel by activityViewModels {
        DrugItemModelFactory((requireActivity().application as SecureApplication).repository)
    }
    private lateinit var binding: FragmentAiChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAiChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button3.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://llm.api.cloud.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(ApiService::class.java)

            val iamToken = System.getenv("IAM_TOKEN")
            val apiKey = "AQVN1zq4foy04zIpJh_z51ySXlSlMPgvaTbe-A3i"


            val authHeaders =
                if (iamToken != null) "Bearer $iamToken" else "Api-Key $apiKey"
            val context = requireContext()
            val assetManager = context.assets
            val inputStream = assetManager.open("body.json")
            val data = inputStream.bufferedReader().use { it.readText() }
            val body =
                data.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            service.gpt(authHeaders, body).enqueue(object : Callback<Root> {
                override fun onResponse(call: Call<Root>, response: Response<Root>) {
                    if (response.isSuccessful) {

                        //Log.d("RRR", response.body().toString())
                        val root: Root? = response.body()
                        val text = root?.result?.alternatives?.firstOrNull()?.message?.text
                        if (root == null) {
                            Log.d("RRR", "nrt roota wefwfw")
                        } else {
                            binding.test.text = text
                            Log.d("RRR", "root = ${text}")
                        }

//                            root!!.result.alternatives[0].message.text
//                            Log.d("RRR", root.toString())
                    }
                }

                override fun onFailure(call: Call<Root>, t: Throwable) {

                }

            })

        }
    }
}