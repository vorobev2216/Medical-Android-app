package com.example.secure

data class Result(
    val alternatives: List<Alternative>,
    val usage: com.example.secure.Usage,
    val modelVersion: String
)

data class Alternative(
    val message: com.example.secure.Message,
    val status: String
)

data class Message(
    val role: String,
    val text: String
)

data class Usage(
    val inputTextTokens: String,
    val completionTokens: String,
    val totalTokens: String
)

data class Root(
    val result: com.example.secure.Result
)