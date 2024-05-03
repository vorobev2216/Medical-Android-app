package com.example.secure.Retrofit

data class Result(
    val alternatives: List<Alternative>,
    val usage: Usage,
    val modelVersion: String
)

data class Alternative(
    val message: Message,
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
    val result: Result
)