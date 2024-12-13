package com.example.linkedup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.databinding.FragmentJobHistoryBinding
import com.example.linkedup.item.HomeAdapter
import com.example.linkedup.item.HomeViewModel


class JobHistoryFragment : Fragment() {
    private lateinit var binding: FragmentJobHistoryBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var lokerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobHistoryBinding.inflate(inflater, container, false)

        lokerView = binding.itemloker
        lokerView.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)

        homeViewModel.historyLiveData.observe(viewLifecycleOwner) { lokerList ->
            Log.d("JobRepository", "Fetched jobs: $lokerList")
            val lokerAdapter = HomeAdapter(
                { id -> id },
                { id, title, deskripsi, gaji, company -> id },
                { id, title, gaji, deskripsi, waktu, company, image ->
                    val intent = Intent(activity, DetailActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("title", title)
                    intent.putExtra("gaji", gaji)
                    intent.putExtra("deskripsi", deskripsi)
                    intent.putExtra("waktu", waktu)
                    intent.putExtra("company", company)
                    intent.putExtra("image", image)
                    startActivity(intent)
                },
                false
            )
            lokerView.adapter = lokerAdapter
            lokerList.let {
                lokerAdapter.submitList(it)
            }
            lokerAdapter.submitList(lokerList)
        }

        return binding.root
    }
}