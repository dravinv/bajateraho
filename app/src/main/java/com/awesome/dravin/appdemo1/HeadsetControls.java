package com.awesome.dravin.appdemo1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

/**
 * Created by Dravin on 6/29/2016.
 */
public class HeadsetControls extends BroadcastReceiver {

    private int status = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("Headset_Plugged"))
        {
            Intent service = new Intent(context, MusicService.class).setAction("PlaySong");
            context.startService(service);
            Toast.makeText(context, "Headset Controls Started", Toast.LENGTH_SHORT).show();
        }
        /*AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (am.isWiredHeadsetOn()) {
            // handle headphones plugged in
            }*/
    }
}
