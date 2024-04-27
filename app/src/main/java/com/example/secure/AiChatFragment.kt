package com.example.secure

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secure.databinding.FragmentAiChatBinding
import com.google.gson.Gson
import com.google.gson.JsonObject
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
    private var massageList = mutableListOf<AiMessage>()
    private val adapter = AiChatAdapter(massageList)

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
        binding.rcChat.adapter = adapter
        binding.rcChat.layoutManager = LinearLayoutManager(requireContext())
        binding.rcChat.isNestedScrollingEnabled = false

        binding.sendBtn.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://llm.api.cloud.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val test = binding.edDrugDesc.text
            Toast.makeText(requireContext(), "$test", Toast.LENGTH_SHORT).show()

            addMessage(binding.edDrugDesc.text.toString(),true)



            val service = retrofit.create(ApiService::class.java)

            val iamToken = System.getenv("IAM_TOKEN")
            val apiKey = "AQVN1zq4foy04zIpJh_z51ySXlSlMPgvaTbe-A3i"


            val authHeaders =
                if (iamToken != null) "Bearer $iamToken" else "Api-Key $apiKey"
            val context = requireContext()
            val assetManager = context.assets
            val inputStream = assetManager.open("body.json")
//            val data = "{\n" +
//                    "  \"modelUri\": \"gpt://b1ge0d6kl2g1ugnc2fo6/yandexgpt-lite\",\n" +
//                    "  \"completionOptions\": {\n" +
//                    "    \"stream\": false,\n" +
//                    "    \"temperature\": 0.6,\n" +
//                    "    \"maxTokens\": \"2000\"\n" +
//                    "  },\n" +
//                    "  \"messages\": [\n" +
//                    "    {\n" +
//                    "      \"role\": \"system\",\n" +
//                    "      \"text\": \"Ты умный ассистент\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "      \"role\": \"user\",\n" +
//                    "      \"text\": \"${binding.edDrugDesc.text}\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "      \"role\": \"assistant\",\n" +
//                    "      \"text\": \"Привет! По каким предметам?\"\n" +
//                    "    },\n" +
//                    "  ]\n" +
//                    "}"

            val data = inputStream.bufferedReader().use { it.readText() }
            val gson = Gson()
            val jsonObj = gson.fromJson(data, JsonObject::class.java)
            val messageArrry = jsonObj.getAsJsonArray("messages")
            for (m in messageArrry) {
                val messageObj = m.asJsonObject
                if (messageObj.get("role").asString == "user") {
                    messageObj.addProperty("text", binding.edDrugDesc.text.toString())
                }
            }

            val mod = gson.toJson(jsonObj)
            Log.d("RRR", mod)
            val body =
                mod.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            service.gpt(authHeaders, body).enqueue(object : Callback<Root> {
                override fun onResponse(call: Call<Root>, response: Response<Root>) {
                    if (response.isSuccessful) {
                        val root: Root? = response.body()
                        val text = root?.result?.alternatives?.firstOrNull()?.message?.text
//                        addMessage(text, false)
                        if (root == null) {
                            Toast.makeText(
                                requireContext(),
                                "Бот временно недоступен",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Log.d("RRR",text!!)
                            addMessage(text, false)
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

    private fun addMessage(text: String?, isUser: Boolean) {
        val messge = text?.let { AiMessage(it, isUser) }
        if (messge != null) {
            massageList.add(messge)
        }
        adapter.notifyItemInserted(massageList.size - 1)

    }
}