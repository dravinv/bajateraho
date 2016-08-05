package com.awesome.dravin.appdemo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class PlayOrReceive extends AppCompatActivity {

    Button music,video,receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_or_receive);

        music = (Button)findViewById(R.id.buttonMusic);
        music.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PlayOrReceive.this, MusicList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                }
        );

        video = (Button)findViewById(R.id.buttonVideo);
        video.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PlayOrReceive.this, VideoList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                        startActivity(intent);
                    }
                }
        );

        receive = (Button)findViewById(R.id.buttonReceive);
        receive.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PlayOrReceive.this,TestActivity.class);//.putExtra("name","null").putExtra("flag",0);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                        startActivity(intent);
                    }
                }
        );

    }

}
