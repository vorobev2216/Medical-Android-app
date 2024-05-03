package com.example.secure.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.secure.R


data class AiMessage(
    val text: String,
    val isUserMessage: Boolean
)


class ChatViewHolder(item: View): RecyclerView.ViewHolder(item){

    private val messageTextView = item.findViewById<TextView>(R.id.chat_tv)
    private val constraintLayout = item.findViewById<ConstraintLayout>(R.id.constraint)


    fun bind(message: AiMessage) {
        messageTextView.apply {
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            val bias = if (message.isUserMessage) 1f else 0f
            constraintSet.setHorizontalBias(id, bias)
            constraintSet.applyTo(constraintLayout)

            text = message.text
        }
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