package com.example.divicemonitor2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FindActivity extends Fragment {


    private static final String SECTION_STRING = "fragment_string";

    public static FindActivity newInstance(String sectionNumber) {
        FindActivity fragment = new FindActivity();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_find,container,false);
        TextView textView=view.findViewById(R.id.textView3);
        textView.setText(R.string.findactivity);


        return view;
    }
}
