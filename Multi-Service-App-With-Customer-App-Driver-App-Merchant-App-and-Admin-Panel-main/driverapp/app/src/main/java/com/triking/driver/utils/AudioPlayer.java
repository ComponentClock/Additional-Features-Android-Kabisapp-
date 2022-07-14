package com.triking.driver.utils;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class AudioPlayer {

    private Context mContext;
    Ringtone defaultRingtone;

    public AudioPlayer(Context context) {
        this.mContext = context.getApplicationContext();
        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context.getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        defaultRingtone = RingtoneManager.getRingtone(context, defaultRingtoneUri);
    }



    public void playRingtone() {
        if (defaultRingtone != null)
            defaultRingtone.play();
    }

    public void stopRingtone() {
        if (defaultRingtone != null)
            defaultRingtone.stop();
    }



}
