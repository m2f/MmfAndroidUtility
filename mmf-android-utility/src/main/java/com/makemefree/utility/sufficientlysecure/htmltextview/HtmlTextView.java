/*
 * Copyright (C) 2013-2014 Dominik Schürmann <dominik@dominikschuermann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.makemefree.utility.sufficientlysecure.htmltextview;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import com.makemefree.utility.R;
import java.io.InputStream;
import java.util.Scanner;

public class HtmlTextView extends JellyBeanSpanFixTextView {

    public static final String TAG = "HtmlTextView";
    public static final boolean DEBUG = false;
    private boolean enableHtmlGetter;

    boolean linkHit;
    @Nullable
    private ClickableTableSpan clickableTableSpan;
    @Nullable
    private DrawTableLinkSpan drawTableLinkSpan;

    boolean dontConsumeNonUrlClicks = true;
    private boolean removeFromHtmlSpace = true;

    public HtmlTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HtmlTextView(Context context) {
        super(context);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HtmlTextView, 0, 0);
        String htmlContent = typedArray.getString(R.styleable.HtmlTextView_htmlContent);
        enableHtmlGetter = typedArray.getBoolean(R.styleable.HtmlTextView_enableImageGetter, false);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.HtmlTextView_textSize, 0);
        String textColor = typedArray.getString(R.styleable.HtmlTextView_textColor);
        Float lineSpacing = typedArray.getFloat(R.styleable.HtmlTextView_lineSpacing, 0.0f);
        typedArray.recycle();

        if(textSize > 0) this.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        if(null != textColor) this.setTextColor(Color.parseColor(textColor));
        if(lineSpacing > 0) this.setLineSpacing(0.0f, lineSpacing);

        if(null != htmlContent) {
            setHtmlContent(htmlContent);
        }
    }

    public void setHtmlContent(String htmlContent) {
        if (null == htmlContent) htmlContent = "";
        if (enableHtmlGetter) {
            setHtml(htmlContent, new HtmlHttpImageGetter(this));
        } else {
            setHtml(htmlContent);
        }
    }
        /**
         *
         * @see HtmlTextView#setHtml(int)
         * @param resId
         */
    public void setHtml(@RawRes int resId) {
        setHtml(resId, null);
    }

    /**
     * @see HtmlTextView#setHtml(String)
     * @param html
     */
    public void setHtml(@NonNull String html) {
        setHtml(html, null);
    }

    /**
     * Loads HTML from a raw resource, i.e., a HTML file in res/raw/.
     * This allows translatable resource (e.g., res/raw-de/ for german).
     * The containing HTML is parsed to Android's Spannable format and then displayed.
     *
     * @param resId       for example: R.raw.help
     * @param imageGetter for fetching images. Possible ImageGetter provided by this library:
     *                    HtmlLocalImageGetter and HtmlRemoteImageGetter
     */
    public void setHtml(@RawRes int resId, @Nullable Html.ImageGetter imageGetter) {
        // load html from html file from /res/raw
        InputStream inputStreamText = getContext().getResources().openRawResource(resId);

        setHtml(convertStreamToString(inputStreamText), imageGetter);
    }

    /**
     * Parses String containing HTML to Android's Spannable format and displays it in this TextView.
     * Using the implementation of Html.ImageGetter provided.
     *
     * @param html        String containing HTML, for example: "<b>Hello world!</b>"
     * @param imageGetter for fetching images. Possible ImageGetter provided by this library:
     *                    HtmlLocalImageGetter and HtmlRemoteImageGetter
     */
    public void setHtml(@NonNull String html, @Nullable Html.ImageGetter imageGetter) {
        if(null == html || html.trim().isEmpty()){
            this.setVisibility(GONE);
        } else {
            this.setVisibility(VISIBLE);
            // this uses Android's Html class for basic parsing, and HtmlTagHandler
            final HtmlTagHandler htmlTagHandler = new HtmlTagHandler();
            htmlTagHandler.setClickableTableSpan(clickableTableSpan);
            htmlTagHandler.setDrawTableLinkSpan(drawTableLinkSpan);
            if (removeFromHtmlSpace) {
                setText(removeHtmlBottomPadding(Html.fromHtml(html, imageGetter, htmlTagHandler)));
            } else {
                setText(Html.fromHtml(html, imageGetter, htmlTagHandler));
            }
            // make links work
            setMovementMethod(LocalLinkMovementMethod.getInstance());
        }
    }

    /**
     * Note that this must be called before setting text for it to work
     * @param removeFromHtmlSpace
     */
    public void setRemoveFromHtmlSpace(boolean removeFromHtmlSpace) {
        this.removeFromHtmlSpace = removeFromHtmlSpace;
    }

    public void setClickableTableSpan(@Nullable ClickableTableSpan clickableTableSpan) {
        this.clickableTableSpan = clickableTableSpan;
    }

    public void setDrawTableLinkSpan(@Nullable DrawTableLinkSpan drawTableLinkSpan) {
        this.drawTableLinkSpan = drawTableLinkSpan;
    }

    /**
     * http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
     */
    @NonNull
    static private String convertStreamToString(@NonNull InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * Html.fromHtml sometimes adds extra space at the bottom.
     * This methods removes this space again.
     * See https://github.com/SufficientlySecure/html-textview/issues/19
     */
    @Nullable
    static private CharSequence removeHtmlBottomPadding(@Nullable CharSequence text) {
        if (text == null) {
            return null;
        }

        while (text.length() > 0 && text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }

        return text;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        linkHit = false;
        boolean res = super.onTouchEvent(event);

        if (dontConsumeNonUrlClicks) {
            return linkHit;
        }
        return res;
    }
}
