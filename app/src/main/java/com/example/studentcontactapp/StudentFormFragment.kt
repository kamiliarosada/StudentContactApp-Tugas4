package com.example.studentcontactapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.studentcontactapp.database.AppDatabase
import com.example.studentcontactapp.database.entity.StudentEntity
import kotlinx.coroutines.launch

class StudentFormFragment : Fragment() {

    private var studentId: Int = -1
    private val prodiList = listOf("Teknik Informatika", "Sistem Informasi", "Teknik Elektro", "Manajemen Informatika")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_student_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitle = view.findViewById<TextView>(R.id.tvFormTitle)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etNim = view.findViewById<EditText>(R.id.etNim)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etSemester = view.findViewById<EditText>(R.id.etSemester)
        val spProdi = view.findViewById<Spinner>(R.id.spProdi)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, prodiList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProdi.adapter = adapter

        val db = AppDatabase.getDatabase(requireContext())
        val studentDao = db.studentDao()

        arguments?.getInt("STUDENT_ID", -1)?.let { id ->
            if (id != -1) {
                studentId = id
                tvTitle.text = "Edit Mahasiswa"
                lifecycleScope.launch {
                    studentDao.getStudentById(id)?.let { student ->
                        etName.setText(student.name)
                        etNim.setText(student.nim)
                        etEmail.setText(student.email)
                        etSemester.setText(student.semester.toString())
                        val prodiIndex = prodiList.indexOf(student.prodi)
                        if (prodiIndex >= 0) spProdi.setSelection(prodiIndex)
                    }
                }
            }
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val nim = etNim.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val semesterStr = etSemester.text.toString().trim()
            val prodi = spProdi.selectedItem.toString()

            if (name.isEmpty() || nim.isEmpty() || email.isEmpty() || semesterStr.isEmpty()) {
                Toast.makeText(context, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val semester = semesterStr.toIntOrNull() ?: 1

            lifecycleScope.launch {
                val student = StudentEntity(
                    id = if (studentId == -1) 0 else studentId,
                    name = name,
                    nim = nim,
                    prodi = prodi,
                    email = email,
                    semester = semester
                )

                if (studentId == -1) {
                    studentDao.insert(student)
                    Toast.makeText(context, "Mahasiswa berhasil ditambah", Toast.LENGTH_SHORT).show()
                } else {
                    studentDao.update(student)
                    Toast.makeText(context, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                }
                parentFragmentManager.popBackStack()
            }
        }
    }
}