package com.example.studentcontactapp

import android.os.Bundle
import android.app.AlertDialog
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentcontactapp.database.AppDatabase
import com.example.studentcontactapp.database.dao.StudentDao
import com.example.studentcontactapp.database.entity.StudentEntity
import com.example.studentcontactapp.utils.PrefManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pref = PrefManager(requireContext())
        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcome)
        tvWelcome.text = "Welcome, ${pref.getUsername()}!"

        val rvStudents = view.findViewById<RecyclerView>(R.id.rvStudents)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAdd)

        val db = AppDatabase.getDatabase(requireContext())
        val studentDao = db.studentDao()

        rvStudents.layoutManager = LinearLayoutManager(requireContext())

        setupSwipeToDelete(rvStudents, studentDao)

        lifecycleScope.launch {
            studentDao.getStudentCount().let { count ->
                if (count == 0) {
                    insertSampleData(studentDao)
                }
            }

            studentDao.getAllStudents().collectLatest { students ->
                rvStudents.adapter = StudentAdapter(
                    students,
                    onClick = { student -> openDetail(student.nim) },
                    onDelete = { student -> showDeleteDialog(student, studentDao) }
                )
            }
        }

        fabAdd.setOnClickListener {
            openForm()
        }
    }

    private fun setupSwipeToDelete(recyclerView: RecyclerView, dao: StudentDao) {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val adapter = recyclerView.adapter as? StudentAdapter
                val student = adapter?.getItemAt(position)
                
                if (student != null) {
                    showDeleteDialog(student, dao)
                    adapter.notifyItemChanged(position)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showDeleteDialog(student: StudentEntity, dao: StudentDao) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Mahasiswa")
            .setMessage("Apakah Anda yakin ingin menghapus ${student.name}?")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    dao.deleteById(student.id)
                    Toast.makeText(context, "Mahasiswa berhasil dihapus", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun openForm(studentId: Int = -1) {
        val fragment = StudentFormFragment().apply {
            arguments = Bundle().apply {
                putInt("STUDENT_ID", studentId)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private suspend fun insertSampleData(dao: com.example.studentcontactapp.database.dao.StudentDao) {
        val samples = listOf(
            StudentEntity(name = "Ahmad Fauzi", nim = "22001", prodi = "Teknik Informatika", email = "ahmad@mail.com", semester = 4),
            StudentEntity(name = "Budi Santoso", nim = "22002", prodi = "Sistem Informasi", email = "budi@mail.com", semester = 4),
            StudentEntity(name = "Clara Wijaya", nim = "22003", prodi = "Teknik Elektro", email = "clara@mail.com", semester = 2)
        )
        dao.insertAll(samples)
    }

    private fun openDetail(nim: String) {
        val fragment = DetailFragment().apply {
            arguments = Bundle().apply {
                putString("NIM", nim)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private class StudentAdapter(
        private val list: List<StudentEntity>,
        private val onClick: (StudentEntity) -> Unit,
        private val onDelete: (StudentEntity) -> Unit
    ) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

        fun getItemAt(position: Int): StudentEntity = list[position]

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvName)
            val tvNim: TextView = view.findViewById(R.id.tvNim)
            val tvProdi: TextView = view.findViewById(R.id.tvProdi)
            val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val student = list[position]
            holder.tvName.text = student.name
            holder.tvNim.text = "NIM: ${student.nim}"
            holder.tvProdi.text = "Prodi: ${student.prodi}"
            
            holder.itemView.setOnClickListener { onClick(student) }
            holder.btnDelete.setOnClickListener { onDelete(student) }
        }

        override fun getItemCount() = list.size
    }
}