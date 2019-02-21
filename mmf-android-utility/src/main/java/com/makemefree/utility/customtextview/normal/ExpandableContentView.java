package com.makemefree.utility.customtextview.normal;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.makemefree.utility.R;

public class ExpandableContentView extends LinearLayout implements View.OnClickListener {

    private ExpandableTextView expandableTextView;
    private Button readMoreButton;
    private boolean isCollapsed;
    private int collapsedLines;
    private String moreButtonText;
    private String lessButtonText;

    public ExpandableContentView(Context context) {
        this(context, null);
    }

    public ExpandableContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableContentView, 0, 0);
        String textContent = typedArray.getString(R.styleable.ExpandableContentView_textContent);
        moreButtonText = typedArray.getString(R.styleable.ExpandableContentView_moreButtonText);
        lessButtonText = typedArray.getString(R.styleable.ExpandableContentView_lessButtonText);
        isCollapsed = typedArray.getBoolean(R.styleable.ExpandableContentView_isCollapsed, true);
        collapsedLines = typedArray.getInt(R.styleable.ExpandableContentView_collapsedLines, ExpandableTextView.DEFAULT_COLLAPSED_LINES);
        int animationDuration = typedArray.getInt(R.styleable.ExpandableContentView_animationDuration, ExpandableTextView.DEFAULT_ANIMATION_DURATION);
        typedArray.recycle();

        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null != inflater) {
            inflater.inflate(R.layout.expandable_content_view, this, true);
        }

        expandableTextView = (ExpandableTextView) getChildAt(0);
        expandableTextView.setAnimationDuration(animationDuration);
        expandableTextView.setCollapsedLines(collapsedLines);
        if(isCollapsed) {
            expandableTextView.setExpanded(false);
            expandableTextView.setMaxLines(collapsedLines);
        } else {
            expandableTextView.setExpanded(true);
            expandableTextView.setMaxLines(Integer.MAX_VALUE);
        }
        setTextContent(textContent);

        readMoreButton = (Button) getChildAt(1);
        readMoreButton.setText(isCollapsed ? moreButtonText : lessButtonText);
        readMoreButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        expandableTextView.toggle();
        readMoreButton.setText(expandableTextView.isExpanded() ? lessButtonText : moreButtonText);
    }

    public void setTextContent(String textContent) {
        if(null == textContent) textContent = "";
        expandableTextView.setText(textContent);
        expandableTextView.post(new Runnable() {
            @Override
            public void run() {
                Layout layout = expandableTextView.getLayout();
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
}
