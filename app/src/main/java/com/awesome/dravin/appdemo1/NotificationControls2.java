package com.awesome.dravin.appdemo1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Dravin on 6/29/2016.
 */
public class NotificationControls2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context,MusicService.class).setAction("PlayNext");
        context.startService(service);
    }
}
