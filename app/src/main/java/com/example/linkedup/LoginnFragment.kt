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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.fetch.AuthPrefs
import com.example.linkedup.fetch.LoginRequest
import com.example.linkedup.fetch.RetrofitClient
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

    //    private fun masukAccount(email: String, password: String) {
//        lifecycleScope.launch {
//            try {
//                sessionViewModel.login(email, password)
////                sessionViewModel.userSession.value?.let { Log.d("lalala", it.name) }
//                sessionViewModel.userSession.observe(viewLifecycleOwner) { user ->
//                    if (user != null) {
//                        // Jika user berhasil disimpan di session (login berhasil)
//                        saveUserToPreferences(user)
//                        val intent = Intent(activity, HomeActivity::class.java)
//                        intent.putExtra("EXTRA_USER_ID", user._id) // Ganti dengan nama properti yang sesuai
//                        intent.putExtra("EXTRA_USER_NAME", user.name.toString())
//                        intent.putExtra("EXTRA_USER_DESCRIPTION", user.deskripsi)
//                        startActivity(intent)
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            Toast.makeText(requireContext(), "Login Berhasil", Toast.LENGTH_SHORT).show()
//                        }, 100)
//                    } else {
////                         Jika userSession tetap null (login gagal)
//                        Toast.makeText(requireContext(), "Login gagal", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("LokerActivity", "Error inserting data", e)
//                Toast.makeText(requireContext(), "Login gagal", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
    private fun saveUserToPreferences(user: User) {
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("user_id", user._id)
            putString("user_name", user.name)
            putString("user_alamat", user.alamat)
            putString("user_email", user.email)
            putString("user_password", user.password) // Hati-hati menyimpan password, gunakan metode hashing dan salting jika perlu
            putString("user_deskripsi", user.deskripsi)
            putString("user_gender", user.jenis_kelamin)
            putBoolean("user_isAdmin", user.isAdmin)
            putString("user_image", user.image)
            apply() // Simpan perubahan
        }
    }
}
