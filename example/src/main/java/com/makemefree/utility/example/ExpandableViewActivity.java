package com.makemefree.utility.example;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.makemefree.utility.example.databinding.ActivityExpandableViewBinding;
import com.makemefree.utility.example.model.NewsItem;

public class ExpandableViewActivity extends Activity {

    private ActivityExpandableViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expandable_view);
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

        // create dummy item
        NewsItem item = new NewsItem();
        item.setHtml(content);
        item.setText(this.getString(R.string.simple_text_content));
        item.setArrayContent(pointers);

        // in XML we declared a variable newsItem, data binding generated the set method
        // once set, all fields/values/views are updated accordingly

        binding.setNewsItem(item);
    }
}
