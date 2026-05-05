package com.example.studentcontactapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class NoteListFragment : Fragment() {

    data class NoteFile(val name: String, val size: Long)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvNotes = view.findViewById<RecyclerView>(R.id.rvNotes)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)
        
        rvNotes.layoutManager = LinearLayoutManager(context)

        val noteFiles = getNoteFiles()
        
        if (noteFiles.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            rvNotes.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            rvNotes.visibility = View.VISIBLE
            rvNotes.adapter = NoteAdapter(noteFiles)
        }
    }

    private fun getNoteFiles(): List<NoteFile> {
        val files = requireContext().fileList()
        return files.filter { it.startsWith("note_") && it.endsWith(".txt") }
            .map { fileName ->
                val file = File(requireContext().filesDir, fileName)
                NoteFile(fileName, file.length())
            }
    }

    private class NoteAdapter(private val notes: List<NoteFile>) :
        RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

        class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvFileName)
            val tvSize: TextView = view.findViewById(R.id.tvFileSize)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_note_file, parent, false)
            return NoteViewHolder(view)
        }

        override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
            val note = notes[position]
            holder.tvName.text = note.name
            holder.tvSize.text = "${note.size} bytes"
        }

        override fun getItemCount() = notes.size
    }
}