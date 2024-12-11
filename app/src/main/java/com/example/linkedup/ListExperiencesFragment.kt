package com.example.linkedup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkedup.databinding.FragmentListExperiencesBinding
import com.example.linkedup.utils.Experience
import com.example.linkedup.item.ExperienceAdapter
import com.example.linkedup.Profile.TambahExperienceFragment
import com.example.linkedup.item.ExperienceViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels
import com.example.linkedup.item.SessionViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListExperiencesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListExperiencesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentListExperiencesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExperienceViewModel by viewModels()
    private val sessionViewModel: SessionViewModel by viewModels({ requireActivity() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListExperiencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionViewModel.userId.observe(viewLifecycleOwner) { userId ->
            userId?.let { viewModel.setUserId(it) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.experiences.collect { experiences ->
                binding.recycleExperience.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = ExperienceAdapter(experiences)
                }
            }
        }

        binding.tambahFloatingExperience.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TambahExperienceFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListExperiencesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListExperiencesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}