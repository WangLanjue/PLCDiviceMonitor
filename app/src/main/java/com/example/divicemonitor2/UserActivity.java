package com.example.divicemonitor2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserActivity extends Fragment {

    private static final String SECTION_STRING = "fragment_string";

    public static UserActivity newInstance(String sectionNumber) {
        UserActivity fragment = new UserActivity();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_user,container,false);
        //TextView textView=view.findViewById(R.id.textView5);
        //textView.setText(R.string.findactivity);
        return view;
    }

}
