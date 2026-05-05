package com.example.studentcontactapp.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream

class FileHelper {

    companion object {
        private fun getFileName(studentNim: String): String {
            return "note_$studentNim.txt"
        }

        fun saveNote(context: Context, studentNim: String, content: String) {
            val fileName = getFileName(studentNim)
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                output.write(content.toByteArray())
            }
        }

        fun loadNote(context: Context, studentNim: String): String {
            val fileName = getFileName(studentNim)
            val file = File(context.filesDir, fileName)
            if (!file.exists()) return ""
            
            return context.openFileInput(fileName).bufferedReader().use { reader ->
                reader.readText()
            }
        }

        fun deleteNote(context: Context, studentNim: String): Boolean {
            val fileName = getFileName(studentNim)
            val file = File(context.filesDir, fileName)
            return if (file.exists()) {
                file.delete()
            } else {
                false
            }
        }

        fun isNoteExists(context: Context, studentNim: String): Boolean {
            val fileName = getFileName(studentNim)
            val file = File(context.filesDir, fileName)
            return file.exists()
        }
        
        fun getFileSize(context: Context, studentNim: String): Long {
            val fileName = getFileName(studentNim)
            val file = File(context.filesDir, fileName)
            return if (file.exists()) file.length() else 0L
        }
    }
}