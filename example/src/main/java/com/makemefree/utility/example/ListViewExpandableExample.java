package com.makemefree.utility.example;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.makemefree.utility.example.databinding.ActivityListViewExpandableExampleBinding;

public class ListViewExpandableExample extends Activity {

    private ActivityListViewExpandableExampleBinding binding;
    private final String[] array = {"Hello", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_view_expandable_example);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.view_row, R.id.header_text, array);
        binding.listview.setAdapter(arrayAdapter);
    }
}
