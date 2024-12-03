package com.example.linkedup

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.item.LokerViewModel
import com.example.linkedup.item.SessionViewModel
import com.example.linkedup.item.UserViewModel
import com.example.linkedup.repository.UserRepository
import com.example.linkedup.utils.Loker
import com.example.linkedup.utils.User
import kotlinx.coroutines.launch

class RegistFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_regist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

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

            userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
            // Regular expression untuk validasi email
            val emailPattern = Regex("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")

            // Logika untuk registrasi (misalnya validasi input)
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                if (email.matches(emailPattern)) {
                    tambahAccount(name, email, password)
                } else {
                    // Tampilkan pesan kesalahan jika format email tidak valid
                    Toast.makeText(requireContext(), "Format email tidak valid", Toast.LENGTH_SHORT).show()
                }
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
    private fun tambahAccount(name: String, email: String, password: String) {
//        val data = User(name = name, email = email, password = password, isAdmin = false, jenis_kelamin = "belum di set", deskripsi = "belum di set", alamat = "belum di set", image = "belum di set")

        lifecycleScope.launch {
            try {
//                userViewModel.insert(data)
                UserRepository().register(name, email, password)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LoginnFragment())
                    .addToBackStack(null)
                    .commit()
                Toast.makeText(requireContext(), "Register Berhasil", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("LokerActivity", "Error inserting data", e)
                Toast.makeText(requireContext(), "Register gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
