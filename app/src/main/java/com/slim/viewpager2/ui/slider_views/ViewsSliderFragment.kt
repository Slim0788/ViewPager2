package com.slim.viewpager2.ui.slider_views

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.slim.viewpager2.R
import com.slim.viewpager2.databinding.FragmentViewsSliderBinding
import com.slim.viewpager2.utils.Utils

class ViewsSliderFragment : Fragment() {

    private lateinit var binding: FragmentViewsSliderBinding

    val layouts = intArrayOf(
            R.layout.slide_one,
            R.layout.slide_two,
            R.layout.slide_three,
            R.layout.slide_four
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentViewsSliderBinding.inflate(inflater, container, false)
        addDots()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pager_transformers, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        binding.viewPager.adapter = ViewsSliderAdapter()
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)

        binding.skipBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.nextBtn.setOnClickListener {
            onNextPage(binding.viewPager.currentItem)
        }
    }

    private fun onNextPage(position: Int) {
        if (position < layouts.size - 1) {
            binding.viewPager.currentItem++
        } else {
            findNavController().popBackStack()
        }
    }

    /*
     * Adds bottom dots indicator
     */
    private fun addDots() {
        val dots = arrayOfNulls<TextView>(layouts.size)
        for (i in dots.indices) {
            dots[i] = TextView(requireContext()).apply {
                text = getString(R.string.dot)
                textSize = 35f
                setTextColor(resources.getColor(R.color.colorInactive))
            }
            binding.dotsLayout.addView(dots[i])
        }
    }

    private fun changeDotsColor(currentPage: Int) {
        val colorsActive = resources.getColor(R.color.colorActive)
        val colorsInactive = resources.getColor(R.color.colorInactive)
        for (view in binding.dotsLayout.children) {
            (view as TextView).setTextColor(colorsInactive)
        }
        (binding.dotsLayout.getChildAt(currentPage) as TextView).setTextColor(colorsActive)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_orientation) {
            if (binding.viewPager.orientation == ViewPager2.ORIENTATION_VERTICAL) {
                binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            } else {
                binding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
            }
        } else {
            binding.viewPager.setPageTransformer(Utils.getTransformer(item.itemId))
        }
        return super.onOptionsItemSelected(item)
    }

    /*
     * ViewPager page change listener
     */
    private var pageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            changeDotsColor(position)

            if (position == layouts.size - 1) {
                binding.nextBtn.text = getString(R.string.start)
                binding.skipBtn.visibility = View.GONE
            } else {
                binding.nextBtn.text = getString(R.string.next)
                binding.skipBtn.visibility = View.VISIBLE
            }
        }
    }

    inner class ViewsSliderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                ViewHolder(LayoutInflater
                        .from(parent.context)
                        .inflate(viewType, parent, false)
                )

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

        override fun getItemViewType(position: Int) = layouts[position]

        override fun getItemCount() = layouts.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }

}