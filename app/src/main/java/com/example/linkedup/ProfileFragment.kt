package com.example.linkedup

import android.app.Activity
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.linkedup.Profile.EditProfileFragment
import com.example.linkedup.databinding.FragmentProfileBinding
import com.example.linkedup.fetch.AuthPrefs
import com.example.linkedup.fetch.ConfigManager
import com.example.linkedup.fetch.RetrofitClient
import com.example.linkedup.item.LokerViewModel
import com.example.linkedup.item.SessionViewModel
import com.example.linkedup.item.UserViewModel
import com.example.linkedup.utils.User
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionViewModel = ViewModelProvider(requireActivity()).get(SessionViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        updateUI()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        user = loadUserFromPreferences() ?: User(-1, "Guest", null, "", "", null, null, false, null)
        binding.hapusAkunButton.setOnClickListener {
            hpsAkun(user._id)
        }


        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        binding.lihatdetailexperience.setOnClickListener {
            val experienceFragment = LihatDetailPengalamanFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, experienceFragment)
                .addToBackStack(null)
                .commit()
        }
        lifecycleScope.launch {
            try {
                Log.d("lalala", "userId value: ${sessionViewModel.userId.value}")
                sessionViewModel.userId.value?.let { Log.d("lalala", it.toString()) }
            } catch (e: Exception) {
                Log.e("Lalalala", "Error inserting data", e)
            }
        }

        binding.tombolEditTabelUser.setOnClickListener{
            val profileEdit = EditProfileFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, profileEdit)
                .addToBackStack(null)
                .commit()
        }
        binding.potoprofil.setOnClickListener {
            val fragmentB = GantiGambarFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentB)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    private fun hpsAkun(userId: Int) {
        AlertDialog.Builder(context)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus Akun ini?")
            .setPositiveButton("Hapus") { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        val response = userViewModel.deleteUserAccount(userId)
                        if (response.isSuccessful) {
                            clearUserPreferences()
                            balikkeLogin()
                        } else {
                            Toast.makeText(context, "Gagal menghapus akun", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("ProfileFragment", "Error hapus data", e)
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


    private fun balikkeLogin() {
        val intent = Intent(activity, RegisterLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    private fun clearUserPreferences() {
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", MODE_PRIVATE)
        with(sharedPref.edit()){
            clear()
            apply()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onResume() {
        super.onResume()
        sessionViewModel.userId.observe(viewLifecycleOwner) { userId ->
            if (userId != null) {
                // Gunakan userId dari sessionViewModel
                Log.d("ProfileFragment", "User ID dari SessionViewModel: $userId")
            } else {
                Log.d("ProfileFragment", "User ID masih null")
            }
        }

        user = loadUserFromPreferences() ?: User(-1, "Guest", null, "", "", null, null, false, null)
        updateUI()
    }

    private fun updateUI() {
        lifecycleScope.launch {
            val token = AuthPrefs.getToken()
            try {
                val response = RetrofitClient.UserApiServices.getMe()

                Log.d("ProfileFragment", "Data berhasil diambil: ${response.name}, Image: ${response.image}")
                // Menampilkan nama, deskripsi, dan alamat
                binding.namauser.text = response.name
                binding.desk.text = response.description
                binding.alamat.text = response.address

                // Menampilkan gambar profil menggunakan Glide
                val BASE_URL = ConfigManager.getBaseUrl()
                val imageUrl = BASE_URL+response.image  // Pastikan response.image berisi URL gambar
                if (imageUrl != null) {
                    Glide.with(requireContext())
                        .load(imageUrl)  // URL gambar yang didapatkan dari API
                        .placeholder(R.drawable.profilegambarstatikfix)  // Gambar placeholder
                        .error(R.drawable.profilegambarstatikfix)  // Gambar error jika gagal load
                        .into(binding.gambarprofil)  // Memasukkan gambar ke ImageView
                }

                // Update gender text
                binding.gender.text = response.gender ?: "(He/Him)"  // Default value if null
            } catch (e: Exception) {
                Log.e("ProfileFragment", "Error loading user data", e)
            }
        }
    }

    private fun loadUserFromPreferences(): User? {
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)
        val userName = sharedPref.getString("user_name", null) ?: return null
        val userAlamat = sharedPref.getString("user_alamat", null)
        val userEmail = sharedPref.getString("user_email", null) ?: return null
        val userPassword = sharedPref.getString("user_password", null) ?: return null // Hati-hati, ini bisa berisiko
        val userDeskripsi = sharedPref.getString("user_deskripsi", null)
        val userGender = sharedPref.getString("user_gender", null)
        val userIsAdmin = sharedPref.getBoolean("user_isAdmin", false)
        val userImage = sharedPref.getString("user_image", null)

        return User(userId, userName, userAlamat, userEmail, userPassword, userDeskripsi, userGender, userIsAdmin, userImage)
    }
    private val AMBIL_GAMBAR_DARI_GALERI = 1
    private fun bukaGaleri(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, AMBIL_GAMBAR_DARI_GALERI)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == AMBIL_GAMBAR_DARI_GALERI && resultCode == Activity.RESULT_OK && data !=null){
//            val gambarUri = data.data
//            Glide.with(this)
//                .load(gambarUri)
//                .placeholder(R.drawable.person1)
//                .error(R.drawable.headtest)
//                .into(binding.potoprofil)
//        }
//    }
}