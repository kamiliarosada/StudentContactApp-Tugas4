package com.example.studentcontactapp.utils

import android.content.Context

class SettingsManager(context: Context) {

    private val pref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private val edit = pref.edit()

    fun getDark(): Boolean = pref.getBoolean("dark", true)
    fun setDark(v: Boolean) = edit.putBoolean("dark", v).apply()

    fun getNotif(): Boolean = pref.getBoolean("notif", true)
    fun setNotif(v: Boolean) = edit.putBoolean("notif", v).apply()

    fun getFont(): Boolean = pref.getBoolean("font", false)
    fun setFont(v: Boolean) = edit.putBoolean("font", v).apply()
}