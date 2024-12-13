package com.example.linkedup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.FragmentJobApplyBinding
import com.example.linkedup.item.ApplyAdapter
import com.example.linkedup.item.CompanyAdapter
import com.example.linkedup.item.HomeAdapter
import com.example.linkedup.item.HomeViewModel
import kotlinx.coroutines.launch


class JobApplyFragment : Fragment() {
    private lateinit var binding: FragmentJobApplyBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var itemView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobApplyBinding.inflate(inflater, container, false)

        itemView = binding.itemloker
        itemView.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)

        homeViewModel.applyLiveData.observe(viewLifecycleOwner) { applyLiveData ->
            applyLiveData?.let {
                itemView.adapter = ApplyAdapter(it, { jobId, userId ->
                    applyJobs(jobId, userId)
                })
            }
        }

        return binding.root
    }

    private fun applyJobs(idJob: String, idUser: String) {
        AlertDialog.Builder(context)
            .setTitle("Konfirmasi Apply")
            .setMessage("Apakah Anda yakin ingin Apply Lamaran Keja ini?")
            .setPositiveButton("Apply") { dialog, _ ->
                lifecycleScope.launch {
                    homeViewModel.acceptApplicant(idJob, idUser)
                    Toast.makeText(requireContext(),"Success add Applicant",Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()

    }

}