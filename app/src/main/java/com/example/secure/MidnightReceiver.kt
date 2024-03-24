package com.example.secure

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent

class MidnightReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context.let {
            val btnState = isButtonEnabled(it!!)
            if (!btnState) {
                it.getSharedPreferences("button", MODE_PRIVATE)
                    .edit()
                    .putLong("midnight", 0L)
                    .apply()
                val i = Intent(context, MainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context?.startActivity(i)
            }
        }

    }

    private fun isButtonEnabled(context: Context): Boolean {
        val currentTime = System.currentTimeMillis()
        val prefsTime = context.getSharedPreferences("button", MODE_PRIVATE).getLong("midnight", 0)

        return currentTime >= prefsTime
    }
}

