package com.awesome.dravin.appdemo1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView)findViewById(R.id.textView2);
        textView.setText("age input... can use a date picker instead.................not required as of now\n" +
                "minm passwordlength............done\n" +
                "age limit.....................done\n" +
                "change of display on clicking signup... no affect on multiple clicks...............done\n" +
                "app comes back to the splash screen twice sometimes..........done\n" +
                "signin should be one time only, googlesign in can be used.........done\n" +
                "pressing enter inside symptom to be disables............................done\n" +
                "pressing suggest multiple time to be disabled...........................done\n" +
                "suggestion tab host should show \n" +
                "sometime even when net connected..no suggestion loaded");
    }

}
