package com.example.divicemonitor2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ConnectActivity extends Fragment {
    private static final String SECTION_STRING = "fragment_string";
    //界面控件
    public static EditText homeporttext;
    public static EditText serveriptext;
    public static EditText serverporttext;
    private Button savechange;

    public static ConnectActivity newInstance(String sectionNumber) {
        ConnectActivity fragment = new ConnectActivity();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_netoption,container,false);


        initview(view);
        showinform();
        savechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPage.homeport= Integer.parseInt(homeporttext.getText().toString());
                MainPage.serverport1=Integer.parseInt(serverporttext.getText().toString());
                MainPage.serverip=serveriptext.getText().toString();
            }
        });

        return view;
    }

    private void initview(View view){
        homeporttext=view.findViewById(R.id.homeport_text);
        serveriptext=view.findViewById(R.id.serverIP_text);
        serverporttext=view.findViewById(R.id.serverport_text);
        savechange=view.findViewById(R.id.save_button);
    }

    private void showinform(){
        homeporttext.setText(""+MainPage.homeport);
        serverporttext.setText(""+MainPage.serverport1);
        serveriptext.setText(""+MainPage.serverip);
    }

}
