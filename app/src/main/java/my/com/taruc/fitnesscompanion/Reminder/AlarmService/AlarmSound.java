package my.com.taruc.fitnesscompanion.Reminder.AlarmService;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

/**
 * Created by saiboon on 20/7/2015.
 */
public class AlarmSound {
    private MediaPlayer player = new MediaPlayer();
    Uri alert;

    // 0 - alarm
    // 1 - notification
    // 2 - ringtone
    public void play(Context context, int soundType) {
        player = new MediaPlayer();
        if(soundType==0) {
            alert = getAlarmSound();
        }else{
            alert = getNotificationSound();
        }
        try {
            player.setDataSource(context, alert);
            final AudioManager audio = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audio.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                player.setAudioStreamType(AudioManager.STREAM_ALARM);
                player.prepare();
                player.start();
            }
        } catch (IOException e) {
            Log.e("Error....", "Check code...");
        }
    }

    public void stop(){
        try {
            player.stop();
        }catch (Exception e) {
            Log.e("Error....", "Check code...");
        }
    }

    public Boolean isPlay(){
        if(player.isPlaying()){
            return true;
        }else{
            return false;
        }
    }

    public Uri getAlarmSound() {
        Uri alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alertSound == null) {
            alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alertSound == null) {
                alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alertSound;
    }

    public Uri getNotificationSound(){
        Uri alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (alertSound == null) {
            alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alertSound == null) {
                alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alertSound;
    }
}
