package com.example.secure

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


data class AiMessage(
    val text: String,
    val isUserMessage: Boolean
)


class ChatViewHolder(item: View): RecyclerView.ViewHolder(item){

    private val messageTextView = item.findViewById<TextView>(R.id.chat_tv)

    fun bind(message: AiMessage) {
        messageTextView.gravity = if(message.isUserMessage) Gravity.END else Gravity.START
        messageTextView.text = message.text
    }


}

class AiChatAdapter(private val message: List<AiMessage>): RecyclerView.Adapter<ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item,parent,false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return message.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(message[position])
    }


}