package com.slim.viewpager2.utils

import com.slim.viewpager2.R
import com.slim.viewpager2.transformers.*

object Utils {
    @JvmStatic
    fun getTransformer(id: Int?) = when (id) {
        R.id.action_anti_clock_spin -> AntiClockSpinTransformation()
        R.id.action_clock_spin -> ClockSpinTransformation()
        R.id.action_cube_in_depth -> CubeInDepthTransformation()
        R.id.action_cube_in_rotate -> CubeInRotationTransformation()
        R.id.action_cube_in_scaling -> CubeInScalingTransformation()
        R.id.action_cube_out_depth -> CubeOutDepthTransformation()
        R.id.action_cube_out_rotate -> CubeOutRotationTransformation()
        R.id.action_cube_out_scaling -> CubeOutScalingTransformation()
        R.id.action_depth_page -> DepthPageTransformer()
        R.id.action_depth -> DepthTransformation()
        R.id.action_fade_out -> FadeOutTransformation()
        R.id.action_fan -> FanTransformation()
        R.id.action_fidget_spin -> FidgetSpinTransformation()
        R.id.action_gate -> GateTransformation()
        R.id.action_hinge -> HingeTransformation()
        R.id.action_horizontal_flip -> VerticalFlipTransformation()
        R.id.action_pop -> PopTransformation()
        R.id.action_simple_transformation -> SimpleTransformation()
        R.id.action_spinner -> SpinnerTransformation()
        R.id.action_toss -> TossTransformation()
        R.id.action_vertical_flip -> HorizontalFlipTransformation()
        R.id.action_vertical_shut -> VerticalShutTransformation()
        R.id.action_zoom_in -> ZoomInTransformer()
        R.id.action_zoom_out -> ZoomOutPageTransformer()
        else -> null
    }
}