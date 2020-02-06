package com.makemefree.utility.example;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.makemefree.utility.example.databinding.ActivityExpandableViewBinding;
import com.makemefree.utility.example.model.NewsItem;

public class ExpandableViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityExpandableViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_expandable_view);
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ut eros sed arcu auctor tincidunt" +
                "    id sit amet elit. Mauris in faucibus neque. Suspendisse facilisis urna nec nisi convallis tincidunt." +
                "    Mauris at elit et arcu viverra auctor. Nullam et arcu ultricies, iaculis dolor efficitur, tristique eros." +
                "    Interdum et malesuada <b>some bold text in here</b> fames ac ante ipsum primis in faucibus." +
                "    Integer nec aliquet mi." +
                "    <ul>" +
                "        <li>One</li>" +
                "        <li>Two</li>" +
                "        <li>Three</li>" +
                "    </ul>" +
                "        <h2><font color='#FF0000'>Know About Me</font></h2>" +
                "        <p>" +
                "            Hello this is in a paragraph, and is long, just joking" +
                "        </p>";

        String[] pointers = new String[]{
                "Lists of advertising characters",
                "Lists of characters from The Office",
                "Lists of Coronation Street characters",
                "Lists of CSI characters",
                "Lists of Emmerdale characters",
                "Lists of fictional Presidents of the United States who was a not a president at all in the history",
                "Lists of Hollyoaks characters",
                "Lists of Stargate characters",
                "Lists of The Walking Dead characters",
                "Lists of Transformers characters",
        };

        StyleSpan styleSpanBold  = new StyleSpan(Typeface.BOLD);
        StyleSpan styleSpanBold1  = new StyleSpan(Typeface.BOLD);
        StyleSpan styleSpanBold2  = new StyleSpan(Typeface.BOLD);


        SpannableString span1 = new SpannableString("Span 1 SpannableString Example.");
        span1.setSpan(styleSpanBold, 0,5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableString span2 = new SpannableString("Span 2 SpannableString Example.");
        span2.setSpan(styleSpanBold1, 0,5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();

        SpannableString span3 = new SpannableString("Span 3 SpannableString Example2.");
        span3.setSpan(strikethroughSpan, 8, 10, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableString span4 = new SpannableString("This is span 4 SpannableString Example2.");
        span4.setSpan(styleSpanBold2, 8, 10, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        SpannableString[] strings = new SpannableString[]{
                span3,
                span2,
                span1,
                span4,
        };


        // create dummy item
        NewsItem item = new NewsItem();
        item.setHtml(content);
        item.setText(this.getString(R.string.simple_text_content));
        item.setArrayContent(strings);

        // in XML we declared a variable newsItem, data binding generated the set method
        // once set, all fields/values/views are updated accordingly

        binding.setNewsItem(item);
    }
}
