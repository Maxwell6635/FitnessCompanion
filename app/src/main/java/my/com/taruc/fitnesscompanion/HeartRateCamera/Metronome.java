package my.com.taruc.fitnesscompanion.HeartRateCamera;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import my.com.taruc.fitnesscompanion.R;


public class Metronome extends Thread {

    Metronome(Context context) {

    }

    @Override
    public void run() {
        while (HeartRateMonitor.bpm != -1)
        {

            int[] time = ArrayUtils.toPrimitive((Integer[]) HeartRateMonitor.bpmQueue.toArray(new Integer[0]));
            int bpm = 1000;

            if (HeartRateMonitor.bpmQueue.size() > 0)
            {
                bpm = 0;


                for (Object b : HeartRateMonitor.bpmQueue)
                {
                    bpm += (Integer)b;
                }
                bpm = bpm / HeartRateMonitor.bpmQueue.size();

            }


            try {
                int msPerBeat = (int) (60f /(bpm + 1) * 1000);
                Log.d(HeartRateMonitor.TAG, "Average BPM:" + bpm + " msPerBeat:" + msPerBeat);
                int sleep = Math.max(200, Math.min(2000, msPerBeat));
                Log.d(HeartRateMonitor.TAG, "sleeping: " + sleep);
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                Log.e(HeartRateMonitor.TAG, "error", e);
            }

        }


    }
}
