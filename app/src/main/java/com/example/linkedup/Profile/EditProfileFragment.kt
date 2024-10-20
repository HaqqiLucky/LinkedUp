package com.example.linkedup.Profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.linkedup.R
import com.example.linkedup.databinding.FragmentEditProfileBinding
import com.example.linkedup.item.UserViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {

    private lateinit var imageView: ImageView
    private val REQUEST_IMAGE_PICK = 1
    private lateinit var binding : FragmentEditProfileBinding
    private lateinit var userViewModel : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        // Inisialisasi ImageView
        imageView = view.findViewById(R.id.gambaryangSesungguhnya)

        // Set onClickListener pada CardView untuk membuka galeri
        val cardView: CardView = view.findViewById(R.id.potoprofil)
        cardView.setOnClickListener {
            selectImageDariGaleri()
        }

        binding = FragmentEditProfileBinding.inflate(inflater,container,false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val id : String = arguments?.getInt("id").toString()
        binding.nama.setText(arguments?.getString("title"))
        binding.deskEdit.setText(arguments?.getString("desk"))
        val selectedGenderId = binding.genderEdit.checkedRadioButtonId
        val selectedGender = when (selectedGenderId){
            R.id.He -> "(He/Him)"
            R.id.She -> "(She/Her)"
            else -> ""
        }
        return view
    }

    private fun selectImageDariGaleri() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Menggunakan Glide untuk memuat gambar yang dipilih ke dalam ImageView
                Glide.with(this)
                    .load(uri)
                    .into(imageView)
            }
        }
    }
}