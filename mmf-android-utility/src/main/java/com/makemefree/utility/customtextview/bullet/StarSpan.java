package com.makemefree.utility.customtextview.bullet;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;
import android.util.TypedValue;

public class StarSpan implements LeadingMarginSpan {

    private final int mGapWidth;
    private final boolean mWantColor;
    private final int mColor;
    private final int mOuterRadius;
    private final int mInnerRadius;
    private final int mSpikes;

    private static Path sStarPath = null;
    public static final int STANDARD_GAP_WIDTH = 8;
    public static final int STANDARD_OUTER_RADIUS = 8;
    public static final int STANDARD_SPIKES = 5;

    public StarSpan(int gapWidth, int outerRadius) {
        this(gapWidth, 0, STANDARD_SPIKES, outerRadius, outerRadius /2);
    }

    public StarSpan(int gapWidth, int color, int spikes, int outerRadius, int innerRadius) {
        mGapWidth = gapWidth;
        mWantColor = color > 0;
        mColor = color;
        mSpikes = spikes;
        mOuterRadius = outerRadius;
        mInnerRadius = innerRadius;
    }

    public int describeContents() {
        return 0;
    }

    public int getLeadingMargin(boolean first) {
        return 2 * mOuterRadius + mGapWidth;
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout l) {
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();
            int oldcolor = 0;

            if (mWantColor) {
                oldcolor = p.getColor();
                p.setColor(mColor);
            }

            p.setStyle(Paint.Style.FILL);

            if (c.isHardwareAccelerated()) {
                Path sStarPath = new Path();
                drawStar(sStarPath, x + dir * mOuterRadius, (top + bottom) / 2.0f);
                c.save();
                //c.translate(x + dir * mStarRadius, (top + bottom) / 2.0f);
                c.drawPath(sStarPath, p);
                c.restore();
            } else {
                //TODO: See how to do when hardware acceleration is not enabled
                //c.drawCircle(x + dir * mStarRadius, (top + bottom) / 2.0f, mStarRadius, p);
            }

            if (mWantColor) {
                p.setColor(oldcolor);
            }

            p.setStyle(style);
        }
    }

    private void drawStar(Path path, float cx, float cy) {

        float rot = (float) (Math.PI/2) * 3;
        float step = (float) (Math.PI/mSpikes);
        path.moveTo(cx, cy - mOuterRadius);

        float x, y;
        for(int i = 0; i < mSpikes; i++) {
            x = (float) (cx + Math.cos(rot) * mOuterRadius);
            y = (float) (cy + Math.sin(rot) * mOuterRadius);
            path.lineTo(x, y);
            rot += step;

            x = (float) (cx + Math.cos(rot) * mInnerRadius);
            y = (float) (cy + Math.sin(rot) * mInnerRadius);
            path.lineTo(x, y);
            rot += step;
        }

        path.lineTo(cx, cy - mOuterRadius);
    }
}
