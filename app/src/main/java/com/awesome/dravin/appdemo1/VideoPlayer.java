package com.awesome.dravin.appdemo1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Intent i = getIntent();

        String action = i.getAction();
        String type = i.getType();

        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("video/mp4".equals(type)) {
                Uri uri = (Uri) i.getParcelableExtra(Intent.EXTRA_STREAM);
                if (uri != null) {
                    videoView.setMediaController(mediaController);
                    videoView.setVideoURI(uri);
                    videoView.requestFocus();
                    videoView.start();
                }
            }
        } else {
            videoView.setMediaController(mediaController);
            Bundle b = i.getExtras();
            String uri = b.getString("uri");
            Uri addr = Uri.parse(uri);
            videoView.setVideoURI(addr);
            videoView.requestFocus();
            videoView.start();
        }
        videoView.setOnCompletionListener(this);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        System.exit(1);
    }
}
