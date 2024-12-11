package com.example.linkedup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkedup.databinding.FragmentListEducationsBinding
import com.example.linkedup.item.EducationAdapter
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.example.linkedup.Profile.TambahEducationFragment
import com.example.linkedup.item.SessionViewModel
import com.example.linkedup.item.EducationViewModel

class ListEducationsFragment : Fragment() {
    private var _binding: FragmentListEducationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var educationViewModel: EducationViewModel
    private lateinit var sessionViewModel: SessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListEducationsBinding.inflate(inflater, container, false)
        educationViewModel = ViewModelProvider(requireActivity())[EducationViewModel::class.java]
        sessionViewModel = ViewModelProvider(requireActivity())[SessionViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionViewModel.userId.observe(viewLifecycleOwner) { userId ->
            userId?.let { educationViewModel.setUserId(it) }
        }

        // Setup RecyclerView
        binding.recycleEducation.layoutManager = LinearLayoutManager(context)
        
        viewLifecycleOwner.lifecycleScope.launch {
            educationViewModel.educations.collect { educations ->
                binding.recycleEducation.adapter = EducationAdapter(
                    educations,
                    onEditClick = { education ->
                        val editFragment = TambahEducationFragment.newInstance(
                            education._id,
                            education.degree
                        )
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, editFragment)
                            .addToBackStack(null)
                            .commit()
                    },
                    onDeleteClick = { education ->
                        showDeleteConfirmationDialog(education._id)
                    }
                )
            }
        }
        
        binding.tambahFloatingEducation.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TambahEducationFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showDeleteConfirmationDialog(educationId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus pendidikan ini?")
            .setPositiveButton("Hapus") { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        val response = educationViewModel.deleteEducation(educationId)
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Education berhasil dihapus", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Gagal menghapus education", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("ListEducation", "Error deleting education", e)
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 