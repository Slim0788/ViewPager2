package com.slim.viewpager2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.slim.viewpager2.R
import com.slim.viewpager2.databinding.FragmentViewsSliderFullscreenBinding
import com.slim.viewpager2.utils.Utils
import kotlinx.android.synthetic.main.view_pager_item.view.*

class FullscreenActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: FragmentViewsSliderFullscreenBinding

    val imageList = intArrayOf(
            R.drawable.img_one,
            R.drawable.img_two,
            R.drawable.img_three,
            R.drawable.img_four,
            R.drawable.img_five,
            R.drawable.img_six,
            R.drawable.img_seven,
            R.drawable.img_eight,
            R.drawable.img_nine,
            R.drawable.img_ten,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentViewsSliderFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addDots()
        binding.viewPager.apply {
            registerOnPageChangeCallback(pageChangeCallback)
            adapter = ViewsSliderAdapter()
        }

        binding.menuIcon.apply {
            visibility = View.VISIBLE
            setOnClickListener { v ->
                showMenu(v)
            }
        }

        binding.previousBtn.setOnClickListener {
            onPreviousPage(binding.viewPager.currentItem)
        }
        binding.nextBtn.setOnClickListener {
            onNextPage(binding.viewPager.currentItem)
        }
    }

    private fun showMenu(view: View) {
        PopupMenu(this, view).apply {
            menuInflater.inflate(R.menu.pager_transformers, this.menu)
            setOnMenuItemClickListener(this@FullscreenActivity)
            show()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_orientation) {
            binding.viewPager.orientation = toggleOrientation(binding.viewPager.orientation)
            Toast.makeText(this@FullscreenActivity, R.string.change_orientation, Toast.LENGTH_SHORT).show()
        } else {
            binding.viewPager.setPageTransformer(Utils.getTransformer(item?.itemId))
        }
        return true
    }

    private fun onPreviousPage(position: Int) {
        if (position > 0) {
            binding.viewPager.currentItem--
        }
    }

    private fun onNextPage(position: Int) {
        if (position < imageList.size - 1) {
            binding.viewPager.currentItem++
        }
    }

    /*
     * Adds bottom dots indicator
     */
    private fun addDots() {
        val dots = arrayOfNulls<TextView>(imageList.size)
        for (i in dots.indices) {
            dots[i] = TextView(this).apply {
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

    private fun toggleOrientation(currentOrientation: Int): Int {
        return if (currentOrientation == ViewPager2.ORIENTATION_VERTICAL) {
            ViewPager2.ORIENTATION_HORIZONTAL
        } else {
            ViewPager2.ORIENTATION_VERTICAL
        }
    }

    private var pageChangeCallback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            changeDotsColor(position)

            when (position) {
                imageList.size - 1 -> {
                    binding.nextBtn.visibility = View.GONE
                }
                imageList.size - 2 -> {
                    binding.nextBtn.visibility = View.VISIBLE
                }
                0 -> {
                    binding.previousBtn.visibility = View.GONE
                }
                1 -> {
                    binding.previousBtn.visibility = View.VISIBLE
                }
            }
        }
    }

    inner class ViewsSliderAdapter : RecyclerView.Adapter<ViewsSliderAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                ViewHolder(LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.view_pager_item, parent, false)
                )

        override fun onBindViewHolder(holder: ViewsSliderAdapter.ViewHolder, position: Int) {
            holder.bind()
        }

        override fun getItemCount() = imageList.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind() {
                itemView.imageView.setImageResource(imageList[adapterPosition])
            }
        }
    }

}