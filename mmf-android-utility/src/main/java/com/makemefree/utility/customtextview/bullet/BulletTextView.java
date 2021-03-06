package com.makemefree.utility.customtextview.bullet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.makemefree.utility.R;

public class BulletTextView extends TextView {

    private int starRadius;
    private int starGapWidth;

    public BulletTextView(Context context) {
        super(context);
    }

    public BulletTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BulletTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BulletTextView, 0, 0);
        int arrayContentRes = typedArray.getResourceId(R.styleable.BulletTextView_arrayContent, -1);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.BulletTextView_textSize, 0);
        String textColor = typedArray.getString(R.styleable.BulletTextView_textColor);
        starGapWidth = typedArray.getDimensionPixelSize(R.styleable.BulletTextView_starGapWidth, -1);
        starRadius = typedArray.getDimensionPixelSize(R.styleable.BulletTextView_starRadius, -1);
        float lineSpacing = typedArray.getFloat(R.styleable.BulletTextView_mmfLineSpacing, 0.0f);
        if(-1 == starGapWidth) {
            starGapWidth = getResources().getDimensionPixelSize(R.dimen.standard_gap_width);
        }

        if(-1 == starRadius) {
            starRadius = getResources().getDimensionPixelSize(R.dimen.standard_outer_radius);
        }

        if(textSize > 0) this.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        if(null != textColor) this.setTextColor(Color.parseColor(textColor));
        if(lineSpacing > 0) this.setLineSpacing(0.0f, lineSpacing);

        String[] arrayContent;
        if(arrayContentRes == -1) {
            arrayContent = new String[0];
        } else {
            arrayContent = getResources().getStringArray(arrayContentRes);
        }
        typedArray.recycle();
        if(arrayContent.length > 0) {
            setArrayContent(arrayContent);
        }
    }

    public void setArrayContent(String[] points) {
        if (null == points || points.length == 0) return;

        String pointersStr = TextUtils.join("\n", points);
        SpannableString span = new SpannableString(pointersStr);
        int spanStart = 0, len = points.length;
        for(int i = 0; i <  len - 1; i++) {
            int spanEnd = spanStart + points[i].length() + 1;
            span.setSpan(new StarSpan(starGapWidth, starRadius), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanStart = spanEnd;
        }
        span.setSpan(new StarSpan(starGapWidth, starRadius), spanStart, pointersStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.setText(span);
    }

    public void setArrayContent(SpannableString[] strings){

        SpannableStringBuilder pointersStr = new SpannableStringBuilder();

        for (int i = 0; i < strings.length; i++) {
            pointersStr.append(strings[i]);
            pointersStr.append("\n");
        }
        int spanStart = 0, len = strings.length;
        for(int i = 0; i <  len - 1; i++) {
            int spanEnd = spanStart + strings[i].length() + 1;
            pointersStr.setSpan(new StarSpan(starGapWidth, starRadius), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanStart = spanEnd;
        }
        pointersStr.setSpan(new StarSpan(starGapWidth, starRadius), spanStart, pointersStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.setText(pointersStr);
    }
}
