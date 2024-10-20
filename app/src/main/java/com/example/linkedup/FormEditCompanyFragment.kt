package com.example.linkedup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

private const val ARG_COMPANY_NAME = "company_name"
private const val ARG_COMPANY_ADDRESS = "company_address"

class FormEditCompanyFragment : Fragment() {
    private var companyName: String? = null
    private var companyAddress: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        // Mengisi EditText dengan param jika ada
        etCompanyName.setText(companyName)
        etCompanyAddress.setText(companyAddress)

        // Event klik tombol Simpan
        btnSimpan.setOnClickListener {
            val name = etCompanyName.text.toString()
            val address = etCompanyAddress.text.toString()
            val website = etCompanyWebsite.text.toString()

            // Implementasi untuk menyimpan data
            Toast.makeText(context, "Data disimpan: $name, $address, $website", Toast.LENGTH_SHORT).show()
        }

        // Event klik tombol Batal
        btnBatal.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        return view
    }

    companion object {
        fun newInstance(companyName: String, companyAddress: String) =
            FormEditCompanyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_COMPANY_NAME, companyName)
                    putString(ARG_COMPANY_ADDRESS, companyAddress)
                }
            }
    }
}
