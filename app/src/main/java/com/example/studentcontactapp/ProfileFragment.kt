package com.example.studentcontactapp

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.studentcontactapp.utils.PrefManager
import com.example.studentcontactapp.utils.SettingsManager

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val pref = PrefManager(requireContext())
        val settings = SettingsManager(requireContext())

        val switchDark = view.findViewById<Switch>(R.id.switchDark)
        val switchFont = view.findViewById<Switch>(R.id.switchFont)
        val switchNotif = view.findViewById<Switch>(R.id.switchNotif)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        switchDark.isChecked = settings.getDark()
        switchFont.isChecked = settings.getFont()
        switchNotif.isChecked = settings.getNotif()

        switchDark.setOnCheckedChangeListener { _, b ->
            settings.setDark(b)
            requireActivity().recreate()
        }

        switchFont.setOnCheckedChangeListener { _, b ->
            settings.setFont(b)
        }

        switchNotif.setOnCheckedChangeListener { _, b ->
            settings.setNotif(b)
        }

        btnLogout.setOnClickListener {
            pref.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
}