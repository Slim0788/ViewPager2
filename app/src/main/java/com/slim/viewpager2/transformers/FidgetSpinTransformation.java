package com.slim.viewpager2.transformers;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class FidgetSpinTransformation implements ViewPager2.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float position) {

        page.setTranslationX(-position * page.getWidth());

        if (Math.abs(position) < 0.5) {
            page.setVisibility(View.VISIBLE);
            page.setScaleX(1 - Math.abs(position));
            page.setScaleY(1 - Math.abs(position));

        } else if (Math.abs(position) > 0.5) {
            page.setVisibility(View.GONE);
        }

        if (position < -1) {     // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        } else {
            final float v = Math.abs(position) * Math.abs(position) * Math.abs(position) * Math.abs(position) * Math.abs(position) * Math.abs(position) * Math.abs(position);

            if (position <= 0) {    // [-1,0]
                page.setAlpha(1);
                page.setRotation(36000 * v);

            } else if (position <= 1) {    // (0,1]
                page.setAlpha(1);
                page.setRotation(-36000 * v);

            } else {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }
        }
    }
}