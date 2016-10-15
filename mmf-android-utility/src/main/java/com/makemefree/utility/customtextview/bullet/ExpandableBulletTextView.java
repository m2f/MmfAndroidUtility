package com.makemefree.utility.customtextview.bullet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

/**
 *
 *  Referenced from :
 * https://codexplo.wordpress.com/2013/09/07/android-expandable-textview/
 * And
 * https://github.com/Blogcat/Android-ExpandableTextView/blob/master/expandabletextview/src/main/java/at/blogc/android/views/ExpandableTextView.java
 *
 */
public class ExpandableBulletTextView extends BulletTextView {

    public static final int DEFAULT_COLLAPSED_LINES = 3;
    public static final int DEFAULT_ANIMATION_DURATION = 250;

    private int collapsedLines;
    private int animationDuration;
    private boolean animating;
    private boolean expanded;
    private int collapsedHeight = 0;
    private int expandedHeight = 0;

    private TimeInterpolator expandInterpolator;
    private TimeInterpolator collapseInterpolator;

    public ExpandableBulletTextView(Context context) {
        this(context, null);
    }

    public ExpandableBulletTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // create default interpolators
        this.expandInterpolator = new AccelerateDecelerateInterpolator();
        this.collapseInterpolator = new AccelerateDecelerateInterpolator();
    }

    public boolean toggle() {
        return this.expanded ? this.collapse() : this.expand();
    }

    /**
     * Expand this {@link ExpandableBulletTextView}.
     * @return true if expanded, false otherwise.
     */
    public boolean expand() {
        if (!this.expanded && !this.animating && this.collapsedLines >= 0) {
            this.animating = true;

            if(0 == collapsedHeight) {
                // get collapsed height
                this.measure(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

                this.collapsedHeight = this.getMeasuredHeight();
            }

            // set maxLines to MAX Integer, so we can calculate the expanded height
            this.setMaxLines(Integer.MAX_VALUE);

            if(0 == expandedHeight) {

                // get expanded height
                this.measure(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

                expandedHeight = this.getMeasuredHeight();
            }

            // animate from collapsed height to expanded height
            final ValueAnimator valueAnimator = ValueAnimator.ofInt(this.collapsedHeight, this.expandedHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation) {
                    final ViewGroup.LayoutParams layoutParams = ExpandableBulletTextView.this.getLayoutParams();
                    layoutParams.height = (int) animation.getAnimatedValue();
                    ExpandableBulletTextView.this.setLayoutParams(layoutParams);
                }
            });

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {
                    // if fully expanded, set height to WRAP_CONTENT, because when rotating the device
                    // the height calculated with this ValueAnimator isn't correct anymore
                    final ViewGroup.LayoutParams layoutParams = ExpandableBulletTextView.this.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    ExpandableBulletTextView.this.setLayoutParams(layoutParams);

                    // keep track of current status
                    ExpandableBulletTextView.this.animating = false;
                }
            });

            // set interpolator
            valueAnimator.setInterpolator(this.expandInterpolator);

            // start the animation
            valueAnimator
                    .setDuration(this.animationDuration)
                    .start();

            this.expanded = true;
            return true;
        }

        return false;
    }

    /**
     * Collapse this {@link TextView}.
     * @return true if collapsed, false otherwise.
     */
    public boolean collapse() {
        if (this.expanded && !this.animating && this.collapsedLines >= 0) {
            this.animating = true;

            if(0 == expandedHeight) {
                // get expanded height
                this.measure(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

                this.expandedHeight = this.getMeasuredHeight();
            }

            if(0 == collapsedHeight) {
                // set maxLines to collapsed lines, so we can calculate the collapsed height
                this.setMaxLines(this.collapsedLines);

                // get collapsed height
                this.measure(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

                this.collapsedHeight = this.getMeasuredHeight();

                // reset max lines and this gets updates on animation end else lines are reduces first and animation complete later
                this.setMaxLines(Integer.MAX_VALUE);
            }


            // animate from expanded height to collapsed height
            final ValueAnimator valueAnimator = ValueAnimator.ofInt(this.expandedHeight, this.collapsedHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation) {
                    final ViewGroup.LayoutParams layoutParams = ExpandableBulletTextView.this.getLayoutParams();
                    layoutParams.height = (int) animation.getAnimatedValue();
                    ExpandableBulletTextView.this.setLayoutParams(layoutParams);
                }
            });

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {
                    // set maxLines to original value
                    ExpandableBulletTextView.this.setMaxLines(ExpandableBulletTextView.this.collapsedLines);

                    // if fully collapsed, set height to WRAP_CONTENT, because when rotating the device
                    // the height calculated with this ValueAnimator isn't correct anymore
                    final ViewGroup.LayoutParams layoutParams = ExpandableBulletTextView.this.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    ExpandableBulletTextView.this.setLayoutParams(layoutParams);

                    ExpandableBulletTextView.this.animating = false;
                }
            });

            // set interpolator
            valueAnimator.setInterpolator(this.collapseInterpolator);

            // start the animation
            valueAnimator
                    .setDuration(this.animationDuration)
                    .start();

            this.expanded = false;
            return true;
        }

        return false;
    }

    public void setAnimationDuration(final int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getCollapsedLines() {
        return collapsedLines;
    }

    public void setCollapsedLines(int collapsedLines) {
        this.collapsedLines = collapsedLines;
    }
}
