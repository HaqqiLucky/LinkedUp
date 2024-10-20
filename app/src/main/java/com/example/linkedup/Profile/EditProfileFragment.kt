package com.example.linkedup.Profile

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.linkedup.HomeActivity
import com.example.linkedup.HomeFragment
import com.example.linkedup.ProfileActivity
import com.example.linkedup.ProfileFragment
import com.example.linkedup.R
import com.example.linkedup.databinding.FragmentEditProfileBinding
import com.example.linkedup.item.UserViewModel
import com.example.linkedup.utils.User
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class EditProfileFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var cardView: CardView // Simpan referensi CardView
    private val REQUEST_IMAGE_PICK = 1
    private val REQUEST_PERMISSION = 2
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var userViewModel: UserViewModel
    private var imagePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        imageView = view.findViewById(R.id.gambaryangSesungguhnya)

        // Set onClickListener pada CardView untuk membuka galeri
        cardView = view.findViewById(R.id.potoprofil) // Simpan referensi ke CardView
        cardView.setOnClickListener {
            selectImageDariGaleri()
        }

        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val user = loadUserFromPreferences() ?: User(-1, "Guest", null, "", "", null, null, false, null)


        binding.nama.setText(user.name)
        binding.deskEdit.setText(user.deskripsi)
        binding.alamatEdit.setText(user.alamat)

        binding.simpanButton.setOnClickListener {
            simpenKeDatabase(user._id, binding.nama.text.toString(), binding.alamatEdit.text.toString(), user.email, user.password, binding.deskEdit.text.toString(), user.isAdmin, user.image.toString())
        }
        return binding.root
    }

    private fun simpenKeDatabase(id: Int, nama: String, alamat: String, email: String, password: String, deskripsi: String, isAdmin: Boolean, image:String) {
        val selectedGenderId = binding.genderEdit.checkedRadioButtonId
        val gender = when (selectedGenderId) {
            R.id.He -> "(He/Him)"
            R.id.She -> "(She/Her)"
            else -> ""
        }

        // Kode ini tidak perlu ada di sini
        // val cardView: CardView = view.findViewById(R.id.potoprofil)
        // cardView.setOnClickListener {
        //     selectImageDariGaleri()
        // }
        val updatedUser = User(
                _id = id,
                name = nama,
                alamat = alamat,
                email = email,
                password = password,
                deskripsi = deskripsi,
                jenis_kelamin = gender,
                isAdmin = isAdmin,
                image = image
            )
        lifecycleScope.launch {
            try {
                userViewModel.update(updatedUser)
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(requireContext(), "Lowongan Data User Berhasil Di Ubah", Toast.LENGTH_SHORT).show()
                }, 100)
            } catch (e: Exception) {
                Log.e("LokerActivity", "Error Updating data", e)
            }
        }

//        if (nama.isNotEmpty() && deskripsi.isNotEmpty() && alamat.isNotEmpty() && imagePath != null) {
//            val updatedUser = User(
//                _id = id, // atau bisa pakai ID user yang sedang diedit jika datanya sudah ada
//                name = nama,
//                alamat = alamat,
//                email = email,
//                password = password,
//                deskripsi = deskripsi,
//                jenis_kelamin = gender,
//                isAdmin = isAdmin,
//                image = image
//            )
//
//            userViewModel.update(updatedUser)
//            Toast.makeText(context, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(context, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun selectImageDariGaleri() {
        if (requireContext().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        } else {
            // Meminta izin
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                selectImageDariGaleri()
            } else {
                Toast.makeText(context, "Izin akses penyimpanan ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Simpan gambar di penyimpanan lokal
                imagePath = saveImageToLocalStorage(uri)

                // Tampilkan gambar yang dipilih
                Glide.with(this)
                    .load(uri)
                    .into(imageView)
            }
        }
    }

    private fun saveImageToLocalStorage(imageUri: Uri): String? {
        // Simpan gambar ke file lokal dan kembalikan path file
        val drawable = imageView.drawable
        var bitmap: Bitmap? = null

        // Cek apakah drawable tidak null
        if (drawable != null) {
            // Jika drawable adalah instance dari BitmapDrawable, langsung dapatkan bitmapnya
            if (drawable is BitmapDrawable) {
                bitmap = drawable.bitmap
            } else {
                // Jika bukan, buat bitmap dari drawable
                bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap!!)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
            }
        }

        var filePath: String? = null

        try {
            val file = File(requireContext().getExternalFilesDir(null), "ppgambar.png")
            val outputStream: OutputStream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Simpan bitmap ke file
            outputStream.flush()
            outputStream.close()

            filePath = file.absolutePath // Kembalikan path gambar
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return filePath
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
}
