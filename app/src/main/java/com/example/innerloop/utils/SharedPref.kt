package com.example.innerloop.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SharedPref {
    fun storeUserData(
        email: String,
        name: String,
        userName: String,
        downloadedUrl: String,
        bio: String,
        context:Context
    ){
        val sharedPreferences = context.getSharedPreferences("userData",MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email",email)
        editor.putString("name",name)
        editor.putString("userName",userName)
        editor.putString("downloadedUrl",downloadedUrl)
        editor.putString("bio",bio)
        editor.apply()
    }

    fun getUserName(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences("userData",MODE_PRIVATE)
        return sharedPreferences.getString("userName","")
    }

    fun getEmail(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences("userData",MODE_PRIVATE)
        return sharedPreferences.getString("email","")
    }

    fun getName(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences("userData",MODE_PRIVATE)
        return sharedPreferences.getString("name","")
    }

    fun getImageUrl(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences("userData",MODE_PRIVATE)
        return sharedPreferences.getString("downloadedUrl","")
    }

    fun getBio(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences("userData",MODE_PRIVATE)
        return sharedPreferences.getString("bio","")
    }
}