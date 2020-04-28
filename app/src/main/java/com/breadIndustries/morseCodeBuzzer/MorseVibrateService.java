package com.breadIndustries.morseCodeBuzzer;

import android.app.IntentService;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

public class MorseVibrateService extends IntentService {
    public Vibrator vibrator;
    public MorseVibrateService() {
        super("MorseVibrateService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        Log.i("MorseVibrateService"," we inside");
        Log.i("messageSMS length", (String.valueOf( intent.getStringExtra("messageSMS").length())));
        String messageSMS = intent.getStringExtra("messageSMS");
        if (messageSMS.length()>0) {
            reMorse m = new reMorse(messageSMS);
            long[] pattern = m.assembler(messageSMS);
            if (0 <= pattern[0]) {
                VibrationEffect effect = VibrationEffect.createWaveform(pattern, -1);
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(effect);
                Log.i("vibratePat", "vibrator");
            }
        }
    }

}
