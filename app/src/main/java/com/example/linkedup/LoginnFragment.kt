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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.fetch.AuthPrefs
import com.example.linkedup.fetch.LoginRequest
import com.example.linkedup.fetch.RetrofitClient
import kotlinx.coroutines.launch

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
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            lifecycleScope.launch {
                val isSuccess = loginUser(email, password)
                if (isSuccess) {
                    val intent = Intent(activity, HomeActivity::class.java)
                        startActivity(intent)
                    Toast.makeText(requireContext(), "Login Berhasil", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Login gagal", Toast.LENGTH_SHORT).show()
                }
            }
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

    suspend fun loginUser(email: String, password: String): Boolean {
        val loginRequest = LoginRequest(email, password)
        val response = RetrofitClient.authApiService.login(loginRequest)

        return if (response.isSuccessful) {
            val token = response.body()?.token ?: ""
            if (token.isNotEmpty()) {
                AuthPrefs.saveToken(token)
                true
            } else {
                false
            }
        } else {
            false
        }
    }
}
