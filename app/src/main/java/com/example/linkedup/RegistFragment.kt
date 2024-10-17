package com.example.linkedup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

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
        val editTextName = view.findViewById<EditText>(R.id.etRegistName)
        val editTextEmail = view.findViewById<EditText>(R.id.etRegistEmail)
        val editTextPassword = view.findViewById<EditText>(R.id.etRegistPassword)
        val buttonRegister = view.findViewById<Button>(R.id.btnRegist)
        val textViewLogin = view.findViewById<TextView>(R.id.tvLogin)

        // Menangani klik pada tombol register
        buttonRegister.setOnClickListener {
            // Mendapatkan nilai dari EditText
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Logika untuk registrasi (misalnya validasi input)
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Lakukan proses registrasi di sini (misalnya, simpan ke database)
                Toast.makeText(requireContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                // Anda dapat melakukan tindakan setelah registrasi sukses
            } else {
                // Tampilkan pesan kesalahan jika ada field yang kosong
                Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

        // Menangani klik pada TextView untuk berpindah ke LoginFragment
        textViewLogin.setOnClickListener {
            val loginFragment = LoginnFragment()

            // Mengganti fragment saat ini dengan LoginFragment
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, loginFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
