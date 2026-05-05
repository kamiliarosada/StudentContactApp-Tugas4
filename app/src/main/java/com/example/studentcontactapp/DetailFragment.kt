package com.example.studentcontactapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

import android.widget.Toast
import com.example.studentcontactapp.utils.FileHelper

import com.example.studentcontactapp.database.AppDatabase
import com.example.studentcontactapp.database.entity.StudentEntity

class DetailFragment : Fragment() {

    private var studentNim: String = "12345"
    private var currentStudent: StudentEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNotes = view.findViewById<EditText>(R.id.etNotes)
        val btnSave = view.findViewById<Button>(R.id.btnSaveNote)
        val btnLoad = view.findViewById<Button>(R.id.btnLoadNote)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val tvInfo = view.findViewById<TextView>(R.id.tvStudentInfo)
        val btnEdit = view.findViewById<Button>(R.id.btnEditStudent)
        val btnDelete = view.findViewById<Button>(R.id.btnDeleteStudent)

        val db = AppDatabase.getDatabase(requireContext())
        val dao = db.studentDao()

        arguments?.getString("NIM")?.let {
            studentNim = it
            lifecycleScope.launch {
                dao.searchStudents(it).collect { students ->
                    val student = students.find { s -> s.nim == it }
                    currentStudent = student
                    student?.let { s ->
                        tvInfo.text = "Nama: ${s.name}\nNIM: ${s.nim}\nProdi: ${s.prodi}\nEmail: ${s.email}\nSemester: ${s.semester}"
                    }
                }
            }
        }

        btnEdit.setOnClickListener {
            currentStudent?.let { s ->
                val fragment = StudentFormFragment().apply {
                    arguments = Bundle().apply {
                        putInt("STUDENT_ID", s.id)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        btnDelete.setOnClickListener {
            currentStudent?.let { s ->
                lifecycleScope.launch {
                    dao.deleteById(s.id)
                    Toast.makeText(context, "Mahasiswa dihapus", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }
        loadNoteContent(etNotes, tvStatus)

        btnSave.setOnClickListener {
            val content = etNotes.text.toString()
            if (content.isNotEmpty()) {
                FileHelper.saveNote(requireContext(), studentNim, content)
                Toast.makeText(context, "Catatan disimpan", Toast.LENGTH_SHORT).show()
                updateStatus(tvStatus)
            } else {
                Toast.makeText(context, "Catatan kosong", Toast.LENGTH_SHORT).show()
            }
        }

        btnLoad.setOnClickListener {
            loadNoteContent(etNotes, tvStatus)
            Toast.makeText(context, "Catatan dimuat", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadNoteContent(etNotes: EditText, tvStatus: TextView) {
        val note = FileHelper.loadNote(requireContext(), studentNim)
        etNotes.setText(note)
        updateStatus(tvStatus)
    }

    private fun updateStatus(tvStatus: TextView) {
        if (FileHelper.isNoteExists(requireContext(), studentNim)) {
            val size = FileHelper.getFileSize(requireContext(), studentNim)
            tvStatus.text = "Status: Tersimpan ($size bytes)"
        } else {
            tvStatus.text = "Status: Belum ada catatan"
        }
    }
}