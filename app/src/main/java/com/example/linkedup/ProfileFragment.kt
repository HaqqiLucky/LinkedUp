package com.example.linkedup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.linkedup.databinding.FragmentPostBinding
import com.example.linkedup.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.lihatdetailexperience.setOnClickListener {
            val experienceFragment = LihatDetailPengalamanFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, experienceFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }


}