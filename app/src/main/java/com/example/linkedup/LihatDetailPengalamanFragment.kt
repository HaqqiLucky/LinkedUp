package com.example.linkedup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedup.item.Experience
import com.example.linkedup.item.ExperienceAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LihatDetailPengalamanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LihatDetailPengalamanFragment : Fragment() {

    private var _binding: LihatDetailPengalamanFragment? = null
    private val binding get() = _binding!!

    private lateinit var itemadapter: ExperienceAdapter

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_lihat_detail_pengalaman,container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclExperience)

        val itemList = listOf(
            Experience("Judul 1","Desk 1"),
            Experience("Judul 2","desk 2"),
            Experience("judul 3","desk 3"),
            Experience("judul 4","desk 4")
        )
        val adapter = ExperienceAdapter(itemList)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LihatDetailPengalamanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LihatDetailPengalamanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}