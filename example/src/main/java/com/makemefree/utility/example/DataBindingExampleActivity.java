/*
 * Copyright (C) 2016 Andhie Wong <andhiewong@gmail.com>
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
package com.makemefree.utility.example;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.makemefree.utility.example.databinding.ActivityDataBindingExampleBinding;
import com.makemefree.utility.example.model.NewsItem;
import com.makemefree.utility.sufficientlysecure.htmltextview.DrawTableLinkSpan;
import com.makemefree.utility.sufficientlysecure.htmltextview.HtmlResImageGetter;
import com.makemefree.utility.sufficientlysecure.htmltextview.HtmlTextView;


public class DataBindingExampleActivity extends Activity {

    // layout_name + binding, generated class
    private ActivityDataBindingExampleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding_example);

        // create dummy item
        NewsItem item = new NewsItem();
        item.setHtml("<p>Interdum et malesuada <b>some bold text in here</b> fames ac ante ipsum primis in faucibus.</p>");

        // in XML we declared a variable newsItem, data binding generated the set method
        // once set, all fields/values/views are updated accordingly
        //binding.setNewsItem(item);

        // if you have set an android:id in XML, data binding do the 'findViewById()'
        DrawTableLinkSpan drawTableLinkSpan = new DrawTableLinkSpan();
        drawTableLinkSpan.setTableLinkText("[tap for table]");
        binding.htmlText.setDrawTableLinkSpan(drawTableLinkSpan);
    }

    /**
     * This method will be used by data binding when we use app:html in XML.
     * BindingAdapters only need to be declared once and usable in the whole app.
     * Its better to put all BindingAdapters in a single Java file.
     *
     * @param view The {@link com.makemefree.utility.sufficientlysecure.htmltextview.HtmlTextView}
     * @param html The value from {@link NewsItem#getHtml()}
     */
    @BindingAdapter({"html"})
    public static void displayHtml(HtmlTextView view, @Nullable String html) {
        view.setHtml(html, new HtmlResImageGetter(view));
    }
}
