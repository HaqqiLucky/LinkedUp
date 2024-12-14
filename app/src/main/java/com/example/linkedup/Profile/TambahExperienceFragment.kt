package com.example.linkedup.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.linkedup.databinding.FragmentTambahExperienceBinding
import com.example.linkedup.item.ExperienceViewModel
import com.example.linkedup.item.SessionViewModel
import kotlinx.coroutines.launch
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.linkedup.R
import com.example.linkedup.fetch.RetrofitClient
import com.example.linkedup.utils.Experience

class TambahExperienceFragment : Fragment() {
    private var _binding: FragmentTambahExperienceBinding? = null
    private val binding get() = _binding!!
    private lateinit var experienceViewModel: ExperienceViewModel
    private var experienceId: Int? = null
    private var currentTitle: String? = null
    private var currentCompany: String? = null
    private var currentIsHighlighted: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahExperienceBinding.inflate(inflater, container, false)
        experienceViewModel = ViewModelProvider(requireActivity())[ExperienceViewModel::class.java]
        
        arguments?.let {
            experienceId = it.getInt("experience_id")
            currentTitle = it.getString("title")
            currentCompany = it.getString("company")
            currentIsHighlighted = it.getBoolean("is_highlighted", false)
            
            binding.Judul.setText(currentTitle)
            binding.namaTempat.setText(currentCompany)
            if (currentIsHighlighted) {
                binding.highlightedEdit.check(R.id.Ya)
            }
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buatButton.setOnClickListener {
            val title = binding.Judul.text.toString()
            val company = binding.namaTempat.text.toString()
            val isHighlighted = binding.highlightedEdit.checkedRadioButtonId == R.id.Ya

            if (title.isEmpty() || company.isEmpty()) {
                Toast.makeText(context, "Mohon isi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                if (experienceId != null) {
                    experienceViewModel.updateExperience(
                        Experience(_id = experienceId!!, title = title, company = company, isHighlighted = isHighlighted)
                    )
                    Toast.makeText(context, "Experience berhasil diupdate", Toast.LENGTH_SHORT).show()
                } else {
                    experienceViewModel.addExperience(title, company, isHighlighted)
                    Toast.makeText(context, "Experience berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                }
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Log.e("TambahExperience", "Error saving experience", e)
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance(experienceId: Int, title: String, company: String, isHighlighted: Boolean): TambahExperienceFragment {
            return TambahExperienceFragment().apply {
                arguments = Bundle().apply {
                    putInt("experience_id", experienceId)
                    putString("title", title)
                    putString("company", company)
                    putBoolean("is_highlighted", isHighlighted)
                }
            }
        }
    }
}