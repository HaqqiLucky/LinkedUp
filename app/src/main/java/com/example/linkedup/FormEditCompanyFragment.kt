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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.item.CompanyViewModel
import com.example.linkedup.utils.Company
import kotlinx.coroutines.launch

private const val ARG_COMPANY_NAME = "company_name"
private const val ARG_COMPANY_ADDRESS = "company_address"

class FormEditCompanyFragment : Fragment() {
    private lateinit var companyViewModel: CompanyViewModel
    private var companyName: String? = null
    private var companyAddress: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        companyViewModel = ViewModelProvider(this).get(CompanyViewModel::class.java)
        arguments?.let {
            companyName = it.getString(ARG_COMPANY_NAME)
            companyAddress = it.getString(ARG_COMPANY_ADDRESS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form_edit_company, container, false)

        // Inisialisasi view
        val etCompanyName = view.findViewById<EditText>(R.id.etCompanyName)
        val etCompanyAddress = view.findViewById<EditText>(R.id.etCompanyAddress)
        val etCompanyWebsite = view.findViewById<EditText>(R.id.etCompanyWebsite)
        val btnSimpan = view.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = view.findViewById<Button>(R.id.btnBatal)

        val id: String = arguments?.getInt("id").toString()

        // Mengisi EditText dengan param jika ada
        etCompanyName.setText(arguments?.getString("nama").toString())
        etCompanyAddress.setText(arguments?.getString("alamat").toString())
        etCompanyWebsite.setText(arguments?.getString("web").toString())

        // Event klik tombol Simpan
        btnSimpan.setOnClickListener {
            val name = etCompanyName.text.toString()
            val address = etCompanyAddress.text.toString()
            val website = etCompanyWebsite.text.toString()

            // Implementasi untuk menyimpan data
            updateCompany(id.toInt(),name,address,website)
        }

        // Event klik tombol Batal
        btnBatal.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CompanyListFragment())
                .commit()
        }

        return view
    }

    private fun updateCompany(id: Int, nama: String, alamat: String, web: String) {
        val data = Company(_id = id, nama = nama, alamat = alamat, web = web)

        lifecycleScope.launch {
            try {
                companyViewModel.update(data)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CompanyListFragment())
                    .commit()
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(requireContext(), "Data Company berhasil diedit", Toast.LENGTH_SHORT).show()
                }, 100)
            } catch (e: Exception) {
                Log.e("LokerActivity", "Error update data", e)
            }
        }
    }
}
