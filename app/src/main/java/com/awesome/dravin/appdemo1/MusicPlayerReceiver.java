package com.awesome.dravin.appdemo1;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Dravin on 6/23/2016.
 */
public class MusicPlayerReceiver extends Activity implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl,
MediaPlayer.OnCompletionListener{
    private static final String TAG = "AudioPlayer";

    public static final String AUDIO_FILE_NAME = "audioFileName";

    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private String audioFile;

    private Handler handler = new Handler();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player_receiver);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String uri = b.getString("uri");
        audioFile = uri;

        ((TextView) findViewById(R.id.now_playing_text_receiver)).setText(uri);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaController = new MediaController(this);

        try {
            mediaPlayer.setDataSource(audioFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e(TAG, "Could not open file " + audioFile + " for playback.", e);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaController.hide();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //the MediaController will hide after 3 seconds - tap the screen to make it appear again
        mediaController.show();
        return false;
    }

    //--MediaPlayerControl methods----------------------------------------------------
    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
//--------------------------------------------------------------------------------

    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared");
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(findViewById(R.id.main_audio_view_receiver));

        handler.post(new Runnable() {
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        System.exit(1);
    }
}
