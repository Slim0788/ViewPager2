package com.slim.viewpager2.ui.slider_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.slim.viewpager2.databinding.FragmentFragmentsSliderBinding
import com.slim.viewpager2.ui.slider_fragments.fragments.EventsFragment
import com.slim.viewpager2.ui.slider_fragments.fragments.MoviesFragment
import com.slim.viewpager2.ui.slider_fragments.fragments.TicketsFragment

class FragmentsSliderFragment : Fragment() {

    // Tab titles
    private val titles = arrayOf(
            "Movies",
            "Events",
            "Tickets"
    )

    private lateinit var binding: FragmentFragmentsSliderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFragmentsSliderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewPager.adapter = FragmentsViewPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    inner class FragmentsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = titles.size

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> return MoviesFragment()
                1 -> return EventsFragment()
                2 -> return TicketsFragment()
                else -> MoviesFragment()
            }
        }
    }

}