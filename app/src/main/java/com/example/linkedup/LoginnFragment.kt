package com.example.linkedup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginnFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loginn, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mendapatkan referensi ke elemen UI
        val editTextEmail = view.findViewById<EditText>(R.id.etLoginEmail) // Disesuaikan dengan ID dari XML
        val editTextPassword = view.findViewById<EditText>(R.id.etLoginPassword) // Disesuaikan dengan ID dari XML
        val buttonLogin = view.findViewById<Button>(R.id.btnLogin) // Disesuaikan dengan ID dari XML
        val textViewRegister = view.findViewById<TextView>(R.id.tvRegister) // Disesuaikan dengan ID dari XML

        // Menangani klik pada tombol login
        buttonLogin.setOnClickListener {
            // Logika untuk login (misalnya validasi input)
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            // Tambahkan logika validasi login di sini
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
        }

        // Menangani klik pada teks untuk berpindah ke registrasi
        textViewRegister.setOnClickListener {
            // Beralih ke RegisterFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegistFragment())
                .addToBackStack(null) // Tambahkan ke back stack agar bisa kembali
                .commit()
        }
    }
}
