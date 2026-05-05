package com.example.studentcontactapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentcontactapp.database.AppDatabase
import com.example.studentcontactapp.database.entity.StudentEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        val rvResults = view.findViewById<RecyclerView>(R.id.rvSearchResults)
        
        rvResults.layoutManager = LinearLayoutManager(requireContext())

        val db = AppDatabase.getDatabase(requireContext())
        val studentDao = db.studentDao()

        performSearch("", studentDao, rvResults)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s.toString(), studentDao, rvResults)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun performSearch(query: String, dao: com.example.studentcontactapp.database.dao.StudentDao, recyclerView: RecyclerView) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            if (query.isEmpty()) {
                dao.getAllStudents().collectLatest { students ->
                    recyclerView.adapter = StudentAdapter(students) { student ->
                        openDetail(student.nim)
                    }
                }
            } else {
                dao.searchStudents(query).collectLatest { students ->
                    recyclerView.adapter = StudentAdapter(students) { student ->
                        openDetail(student.nim)
                    }
                }
            }
        }
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
        private val onClick: (StudentEntity) -> Unit
    ) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvName)
            val tvNim: TextView = view.findViewById(R.id.tvNim)
            val tvProdi: TextView = view.findViewById(R.id.tvProdi)
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
            

            holder.itemView.findViewById<View>(R.id.btnDelete)?.visibility = View.GONE

            holder.itemView.setOnClickListener { onClick(student) }
        }

        override fun getItemCount() = list.size
    }
}