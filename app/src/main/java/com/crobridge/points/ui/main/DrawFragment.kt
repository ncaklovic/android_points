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
import com.crobridge.points.databinding.FragmentDrawBinding
import com.crobridge.points.db.PointDb
import android.util.Log;
import com.crobridge.points.PointViewModel
import com.crobridge.points.ViewModelFactory

/**
 * A placeholder fragment containing a simple view.
 */
class DrawFragment : Fragment() {

    /*private*/ lateinit var viewModel: PointViewModel
    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentDrawBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("DrawFragment", "onCreate");
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("PlaceHolderFragment", "onCreateView");
        _binding = FragmentDrawBinding.inflate(inflater, container, false)
        val root = binding.root

        val app = requireNotNull(this.activity).application
        val dataSource = PointDb.getInstance(app).pointDbDao
        val viewModelFactory = ViewModelFactory(dataSource, app)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PointViewModel::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        //binding.lifecycleOwner = this

        //val drawView: DrawView = binding.drawView
        pageViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
        viewModel.points.observe(viewLifecycleOwner, Observer {
            binding.drawView.invalidate()
        } )

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
            return DrawFragment().apply {
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