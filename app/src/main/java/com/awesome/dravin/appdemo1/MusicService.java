package com.awesome.dravin.appdemo1;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dravin on 6/18/2016.
 */


public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {


    private int noti_play_pause = R.drawable.pause;
    private int notiService = 0;
    private final IBinder musicBind = new MusicBinder();
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;

    private String songTitle = null;
    private static final int NOTIFY_ID = 1;

    private boolean shuffle = false;
    private Random rand;

    private int headSetStatus = 9999;
    private String str;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (notiService == 1) {
            String str = intent.getAction();
            if (str.equals("PlaySong")) {
                if (isPng()) {
                    pausePlayer();
                    noti_play_pause = R.drawable.play_noti;
                    makeNoti();
                } else {
                    go();
                    noti_play_pause = R.drawable.pause;
                    makeNoti();
                }
            }
            if (str.equals("PlayNext")) {
                playNext();
                makeNoti();
            }
            if (str.equals("PlayPrev")) {
                playPrev();
                makeNoti();
            }
        }
        /*AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if (am.isWiredHeadsetOn()) {
            // handle headphones plugged in
            headSetStatus = 1;
        } else {
            headSetStatus++;
        }
        if(headSetStatus==2)
        {
            str = "Headsset_Plugged";
            Intent intent1 = new Intent();
            intent1.setAction(str);
            sendBroadcast(intent1);
        }*/

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onCreate() {
        //create the service

        super.onCreate();
        //initialize position
        songPosn = 0;
        //create player
        player = new MediaPlayer();

        initMusicPlayer();

        rand = new Random();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (player.getCurrentPosition() >= 0) {
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        makeNoti();
        //Intent intent = new Intent(this,HeadsetControls.class);
        //sendBroadcast(intent);
    }

    public void makeNoti() {
        notiService = 1;
        Intent notIntent = new Intent(this, MusicList.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        Intent newIntent = new Intent(this, NotificationControls.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent newIntent1 = new Intent(this, NotificationControls1.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, newIntent1, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent newIntent2 = new Intent(this, NotificationControls2.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, newIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.app_icon_small)
                .setTicker(songTitle)
                .setOngoing(true)
                .addAction(R.drawable.prev, "", pendingIntent1)
                .addAction(noti_play_pause, "", pendingIntent)
                .addAction(R.drawable.next, "", pendingIntent2)
                .setContentTitle("Playing").setContentText(songTitle);
        Notification not = builder.build();
        startForeground(NOTIFY_ID, not);
    }

    public void initMusicPlayer() {
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> theSongs) {
        songs = theSongs;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }



    public void playSong() {
        //play a song
        player.reset();
        //get song
        Song playSong = songs.get(songPosn);

        songTitle = playSong.getTitle();
        //get id
        long currSong = playSong.getID();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    public int getPosn() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void go() {
        player.start();
    }

    public void playPrev() {
        songPosn--;
        if (songPosn == 0) songPosn = songs.size() - 1;
        playSong();
    }

    //skip to next
    public void playNext() {
        if (shuffle) {
            int newSong = songPosn;
            while (newSong == songPosn) {
                newSong = rand.nextInt(songs.size());
            }
            songPosn = newSong;
        } else {
            songPosn++;
            if (songPosn >= songs.size()) songPosn = 0;
        }
        playSong();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public int setShuffle() {
        if (shuffle) {
            shuffle = false;
            return 0;
        } else {
            shuffle = true;
            return 1;
        }
    }

    public void handleHeadphonesState(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (am.isWiredHeadsetOn()) {
            // handle headphones plugged in
        } else {
            if (isPng()) {
                pausePlayer();
            }
            // handle headphones unplugged
        }
    }
}
