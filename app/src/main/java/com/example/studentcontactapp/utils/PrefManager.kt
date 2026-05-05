package com.example.studentcontactapp.utils

import android.content.Context

class PrefManager(context: Context) {

    private val pref = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    fun setLogin(v: Boolean) = editor.putBoolean("is_login", v).apply()
    fun isLogin(): Boolean = pref.getBoolean("is_login", false)

    fun setUsername(u: String) = editor.putString("username", u).apply()
    fun getUsername(): String = pref.getString("username", "") ?: ""

    fun setRemember(v: Boolean) = editor.putBoolean("remember", v).apply()
    fun isRemember(): Boolean = pref.getBoolean("remember", false)

    fun logout() = editor.clear().apply()
}