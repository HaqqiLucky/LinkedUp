package com.example.linkedup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class RegistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_regist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mendapatkan referensi ke elemen UI
        val editTextName = view.findViewById<EditText>(R.id.etRegisterName) // ID untuk EditText nama
        val editTextEmail = view.findViewById<EditText>(R.id.etRegisterEmail) // ID untuk EditText email
        val editTextPassword = view.findViewById<EditText>(R.id.etRegisterPassword) // ID untuk EditText password
        val buttonRegister = view.findViewById<Button>(R.id.btnRegister) // ID untuk Button register

        // Menangani klik pada tombol register
        buttonRegister.setOnClickListener {
            // Mendapatkan nilai dari EditText
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Logika untuk registrasi (misalnya validasi input)
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Lakukan proses registrasi di sini
                // Misalnya, simpan data ke database atau lakukan autentikasi
            } else {
                // Tampilkan pesan kesalahan jika ada field yang kosong
                // Anda bisa menggunakan Toast atau Snackbar
            }
        }
    }
}
