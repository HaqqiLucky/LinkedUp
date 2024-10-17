package com.example.linkedup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.FragmentHomeBinding
import com.example.linkedup.item.LokerAdapter
import com.example.linkedup.item.LokerViewModel
import com.example.linkedup.utils.Loker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var lokerViewModel: LokerViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lokerViewModel = ViewModelProvider(this).get(LokerViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        recyclerView = binding.itemloker  // Pastikan ID ini benar
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lokerViewModel.allLoker.observe(viewLifecycleOwner) { lokerList ->
            lokerList?.let {
                recyclerView.adapter = LokerAdapter(it)
            }
        }

        binding.addpost.setOnClickListener {
            navigateToPostFragment()
        }

        return binding.root
    }

    private fun navigateToPostFragment() {
        val postFragment = PostFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_post, postFragment)
            .addToBackStack(null)
            .commit()
    }

}