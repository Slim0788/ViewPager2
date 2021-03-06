package com.slim.viewpager2.ui.selection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.slim.viewpager2.R
import com.slim.viewpager2.databinding.FragmentSelectionBinding
import com.slim.viewpager2.ui.FullscreenActivity

class SelectionFragment : Fragment() {

    private lateinit var binding: FragmentSelectionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            sliderFragmentsBtn.setOnClickListener {
                findNavController().navigate(R.id.action_selection_to_fragmentsSlider)
            }
            sliderViewsBtn.setOnClickListener {
                findNavController().navigate(R.id.action_selection_to_viewsSlider)
            }
            fullscreenFragmentBtn.setOnClickListener {
                findNavController().navigate(R.id.action_selection_to_fullscreenViewsSlider)
            }
            fullscreenActivityBtn.setOnClickListener {
                startActivity(Intent(requireActivity(), FullscreenActivity::class.java))
            }
        }
    }

}