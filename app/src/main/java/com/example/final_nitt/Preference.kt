package com.example.final_nitt

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Preference(context: Context) {

    private val sharedPreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String){
        sharedPreference.edit(commit = true){
            putString("token", token)
        }
    }

    fun getToken(): String?{
        return sharedPreference.getString("token", "")
    }

    fun deleteToken(){
        sharedPreference.edit(commit = true){
            remove("token")
        }
    }

    companion object{
        const val PREF_NAME = "share_pref"
    }
}