package com.example.linkedup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.item.SessionViewModel
import com.example.linkedup.item.UserViewModel
import com.example.linkedup.utils.User
import kotlinx.coroutines.launch

class LoginnFragment : Fragment() {
    private lateinit var sessionViewModel: SessionViewModel

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

        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)

        // Menangani klik pada tombol login
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            masukAccount(email,password)
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
    private fun masukAccount(email: String, password: String) {
        lifecycleScope.launch {
            try {
                sessionViewModel.login(email, password)
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(requireContext(), "Login Berhasil", Toast.LENGTH_SHORT).show()
                }, 100)
            } catch (e: Exception) {
                Log.e("LokerActivity", "Error inserting data", e)
                Toast.makeText(requireContext(), "Login gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
