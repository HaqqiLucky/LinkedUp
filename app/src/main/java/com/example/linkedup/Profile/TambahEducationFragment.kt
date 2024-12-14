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
import com.example.linkedup.utils.Education

class TambahEducationFragment : Fragment() {
    private var _binding: FragmentTambahEducationBinding? = null
    private val binding get() = _binding!!
    private lateinit var educationViewModel: EducationViewModel
    private var educationId: Int? = null
    private var currentDegree: String? = null
    private var currentSchoolName: String? = null

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
            currentSchoolName = it.getString("school_name")
            binding.degree.setText(currentDegree)
            binding.schoolName.setText(currentSchoolName)
        }

        binding.simpanButton.setOnClickListener {
            val degree = binding.degree.text.toString()
            val schoolName = binding.schoolName.text.toString()

            if (degree.isEmpty() || schoolName.isEmpty()) {
                Toast.makeText(context, "Mohon isi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    if (educationId != null) {
                        educationViewModel.updateEducation(Education(_id = educationId!!, degree = degree, schoolName = schoolName))
                        Toast.makeText(context, "Education berhasil diupdate", Toast.LENGTH_SHORT).show()
                    } else {
                        educationViewModel.addEducation(degree, schoolName)
                        Toast.makeText(context, "Education berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    }
                    parentFragmentManager.popBackStack()
                } catch (e: Exception) {
                    Log.e("TambahEducation", "Error saving education", e)
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(educationId: Int, degree: String, schoolName: String): TambahEducationFragment {
            return TambahEducationFragment().apply {
                arguments = Bundle().apply {
                    putInt("education_id", educationId)
                    putString("degree", degree)
                    putString("school_name", schoolName)
                }
            }
        }
    }
} 