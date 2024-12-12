package com.example.linkedup.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.databinding.FragmentTambahEducationBinding
import com.example.linkedup.item.EducationViewModel
import kotlinx.coroutines.launch
import android.util.Log

class TambahEducationFragment : Fragment() {
    private var _binding: FragmentTambahEducationBinding? = null
    private val binding get() = _binding!!
    private lateinit var educationViewModel: EducationViewModel
    private var educationId: Int? = null
    private var currentDegree: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahEducationBinding.inflate(inflater, container, false)
        educationViewModel = ViewModelProvider(requireActivity())[EducationViewModel::class.java]
        
        arguments?.let {
            educationId = it.getInt("education_id")
            currentDegree = it.getString("degree")
            binding.degree.setText(currentDegree)
        }

        binding.simpanButton.setOnClickListener {
            val degree = binding.degree.text.toString()

            if (degree.isEmpty()) {
                Toast.makeText(context, "Mohon isi degree pendidikan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                    try {
    //                    val response = if (educationId != null) {
    //                        educationViewModel.updateEducation(educationId!!, degree)
    //                    } else {
                        educationViewModel.addEducation(degree)
    //                    }
    //
    //                    if (response.isSuccessful) {
    //                        val message = if (educationId != null) "Education berhasil diupdate" else "Education berhasil ditambahkan"
    //                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    //                        parentFragmentManager.popBackStack()
    //                    } else {
    //                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
    //                        Toast.makeText(context, "Gagal: $errorMessage", Toast.LENGTH_SHORT).show()
    //                    }
                    } catch (e: Exception) {
                        Log.e("TambahEducation", "Error saving education", e)
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(educationId: Int, degree: String): TambahEducationFragment {
            return TambahEducationFragment().apply {
                arguments = Bundle().apply {
                    putInt("education_id", educationId)
                    putString("degree", degree)
                }
            }
        }
    }
} 