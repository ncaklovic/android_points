package com.crobridge.points.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.crobridge.points.R
import android.util.Log;
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.crobridge.points.PointAdapter
import com.crobridge.points.PointListener
import com.crobridge.points.PointViewModel
import com.crobridge.points.ViewModelFactory
import com.crobridge.points.databinding.FragmentGridBinding
import com.crobridge.points.db.PointDb

/**
 * A placeholder fragment containing a simple view.
 */
class GridFragment : Fragment() {

    /*private*/ lateinit var viewModel: PointViewModel
    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentGridBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("GridFragment", "onCreate");
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("GridFragment", "onCreateView");
        _binding = FragmentGridBinding.inflate(inflater, container, false)
        val root = binding.root

        val app = requireNotNull(this.activity).application
        val dataSource = PointDb.getInstance(app).pointDbDao
        val viewModelFactory = ViewModelFactory(dataSource, app)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PointViewModel::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        val manager = LinearLayoutManager(activity)
        binding.pointList.layoutManager = manager

        val adapter = PointAdapter(PointListener { pointId ->
            Toast.makeText(context, "${pointId}", Toast.LENGTH_LONG).show()
            //viewModel.onPointClicked(pointId)
        })

        binding.pointList.adapter = adapter

        viewModel.points.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): Fragment {
            return GridFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}