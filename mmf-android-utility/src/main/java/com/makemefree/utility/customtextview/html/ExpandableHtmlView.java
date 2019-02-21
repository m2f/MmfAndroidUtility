package com.makemefree.utility.customtextview.html;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.makemefree.utility.R;
import com.makemefree.utility.sufficientlysecure.htmltextview.HtmlHttpImageGetter;

public class ExpandableHtmlView extends LinearLayout implements View.OnClickListener {

    private ExpandableHtmlTextView expandableHtmlTextView;
    private Button readMoreButton;
    private boolean isCollapsed;
    private boolean enableHtmlGetter;
    private int collapsedLines;
    private String moreButtonText;
    private String lessButtonText;

    public ExpandableHtmlView(Context context) {
        this(context, null);
    }

    public ExpandableHtmlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableHtmlView, 0, 0);
        String htmlContent = typedArray.getString(R.styleable.ExpandableHtmlView_htmlContent);
        moreButtonText = typedArray.getString(R.styleable.ExpandableHtmlView_moreButtonText);
        lessButtonText = typedArray.getString(R.styleable.ExpandableHtmlView_lessButtonText);
        isCollapsed = typedArray.getBoolean(R.styleable.ExpandableHtmlView_isCollapsed, true);
        enableHtmlGetter = typedArray.getBoolean(R.styleable.ExpandableHtmlView_enableImageGetter, false);
        int collapsedLines = typedArray.getInt(R.styleable.ExpandableHtmlView_collapsedLines, ExpandableHtmlTextView.DEFAULT_COLLAPSED_LINES);
        int animationDuration = typedArray.getInt(R.styleable.ExpandableHtmlView_animationDuration, ExpandableHtmlTextView.DEFAULT_ANIMATION_DURATION);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.ExpandableHtmlView_textSize, 0);
        String textColor = typedArray.getString(R.styleable.ExpandableHtmlView_textColor);
        float lineSpacing = typedArray.getFloat(R.styleable.ExpandableHtmlView_mmfLineSpacing, 0.0f);

        typedArray.recycle();

        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null != inflater) {
            inflater.inflate(R.layout.expandable_html_view, this, true);
        }

        expandableHtmlTextView = (ExpandableHtmlTextView) getChildAt(0);
        if(textSize > 0) expandableHtmlTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        if(null != textColor) expandableHtmlTextView.setTextColor(Color.parseColor(textColor));
        if(lineSpacing > 0) expandableHtmlTextView.setLineSpacing(0.0f, lineSpacing);

        expandableHtmlTextView.setAnimationDuration(animationDuration);
        setCollapsedLines(collapsedLines);
        setHtmlContent(htmlContent);

        readMoreButton = (Button) getChildAt(1);
        readMoreButton.setText(isCollapsed ? moreButtonText : lessButtonText);
        readMoreButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        expandableHtmlTextView.toggle();
        readMoreButton.setText(expandableHtmlTextView.isExpanded() ? lessButtonText : moreButtonText);
    }

    public void setCollapsedLines(int collapsedLines) {
        this.collapsedLines = collapsedLines;
        expandableHtmlTextView.setCollapsedLines(collapsedLines);
        if(isCollapsed) {
            expandableHtmlTextView.setExpanded(false);
            expandableHtmlTextView.setMaxLines(collapsedLines);
        } else {
            expandableHtmlTextView.setExpanded(true);
            expandableHtmlTextView.setMaxLines(Integer.MAX_VALUE);
        }
    }

    public void setHtmlContent(String htmlContent) {
        if(null == htmlContent) htmlContent = "";
        if(enableHtmlGetter) {
            expandableHtmlTextView.setHtml(htmlContent, new HtmlHttpImageGetter(expandableHtmlTextView));
        } else {
            expandableHtmlTextView.setHtml(htmlContent);
        }
        expandableHtmlTextView.post(new Runnable() {
            @Override
            public void run() {
                Layout layout = expandableHtmlTextView.getLayout();
                if(layout != null) {
                    int lineCount = layout.getLineCount();
                    if(lineCount > 0) {
                        int ellipsisCount = layout.getEllipsisCount(lineCount - 1);
                        boolean isVisible = ellipsisCount > 0 || lineCount > collapsedLines;
                        readMoreButton.setVisibility( isVisible ? VISIBLE : GONE);
                    }
                }
            }
        });
    }

    public ExpandableHtmlTextView getExpandableHtmlTextView() {
        return expandableHtmlTextView;
    }
}
