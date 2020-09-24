package com.slim.viewpager2.ui.fullscreen

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.slim.viewpager2.R
import com.slim.viewpager2.databinding.FragmentViewsSliderFullscreenBinding
import com.slim.viewpager2.utils.Utils
import kotlinx.android.synthetic.main.view_pager_item.view.*

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ViewsSliderFullscreenFragment : Fragment() {

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

    private val hideHandler = Handler()

    @Suppress("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        val flags =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.decorView?.systemUiVisibility = flags
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        binding.fullscreenContentControls.visibility = View.VISIBLE
    }
    private var visible: Boolean = false
    private val hideRunnable = Runnable { hide() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val delayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentViewsSliderFullscreenBinding.inflate(inflater, container, false)
        addDots()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pager_transformers, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.viewPager.apply {
            registerOnPageChangeCallback(pageChangeCallback)
            adapter = ViewsSliderAdapter()
        }

        visible = true

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        binding.apply {
            previousBtn.setOnTouchListener(delayHideTouchListener)
            previousBtn.setOnClickListener {
                onPreviousPage(binding.viewPager.currentItem)
            }
            nextBtn.setOnTouchListener(delayHideTouchListener)
            nextBtn.setOnClickListener {
                onNextPage(binding.viewPager.currentItem)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_orientation) {
            binding.viewPager.orientation = toggleOrientation(binding.viewPager.orientation)
            Toast.makeText(requireContext(), R.string.change_orientation, Toast.LENGTH_SHORT).show()
        } else {
            binding.viewPager.setPageTransformer(Utils.getTransformer(item.itemId))
        }
        return super.onOptionsItemSelected(item)
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

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Clear the systemUiVisibility flag
        activity?.window?.decorView?.systemUiVisibility = 0
        show()
    }

    private fun toggle() {
        if (visible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        binding.fullscreenContentControls.visibility = View.GONE
        visible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @Suppress("InlinedApi")
    private fun show() {
        // Show the system bar
        visible = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    /*
     * Adds bottom dots indicator
     */
    private fun addDots() {
        val dots = arrayOfNulls<TextView>(imageList.size)
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

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
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
                // Set up the user interaction to manually show or hide the system UI.
                itemView.setOnClickListener { toggle() }
            }
        }
    }
}