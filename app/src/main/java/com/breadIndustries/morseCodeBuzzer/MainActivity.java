package com.breadIndustries.morseCodeBuzzer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {
    Toast toast;
//    public Vibrator vibrator;
    reMorse m = new reMorse();
    private static final int PERMISSION_READ_CONTACTS = 1;
    private static final int PERMISSION_RECEIVE_SMS = 2;
    MorseSmsReceiver morseSmsReceiver = new MorseSmsReceiver();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(morseSmsReceiver);
        Log.i("on destroy ", "unregistered receiver");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Main","in main");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        initSpinnerWPM();
        Switch switchSMS = findViewById(R.id.switchSMS);
        Activity thisActivity = this;
        switchSMS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ActivityCompat.requestPermissions(thisActivity,new String[]{Manifest.permission.RECEIVE_SMS},PERMISSION_RECEIVE_SMS);
                    IntentFilter filter = new IntentFilter(getPackageName()+"android.provider.Telephony.SMS_RECEIVED");
                    registerReceiver(morseSmsReceiver, filter);
                    Log.i("sms check ", "registered receiver");
                }
                else{
                    unregisterReceiver(morseSmsReceiver);
                    Log.i("sms check ", "unregistered receiver");
                }
            }
        });
        Switch switchContacts = findViewById(R.id.switchContacts);
        switchContacts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ActivityCompat.requestPermissions(thisActivity,new String[]{Manifest.permission.READ_CONTACTS},PERMISSION_READ_CONTACTS);
                }
                else{
                }
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        checkPermSMS();
        checkPermContacts();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode) {
            case PERMISSION_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.i("contacts permission request", " granted!");
//                    Log.i("Permission phone lookup", m.getContactName("+15555215554", this));
                }
            }
            case PERMISSION_RECEIVE_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.i("sms permission request", " granted!");
                } else {
//                    Log.i("sms permission request", " denied");
                }
            }
        }
    }
    private void checkPermSMS(){
        Switch switchSMS = findViewById(R.id.switchSMS);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
            switchSMS.setChecked(true);
        }
        else{
            switchSMS.setChecked(false);
        }
    }
    private void checkPermContacts(){
        Switch switchContacts = findViewById(R.id.switchContacts);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            switchContacts.setChecked(true);
        }
        else{
            switchContacts.setChecked(false);
            Intent intent = new Intent(getApplicationContext(),MorseVibrateService.class);
            intent.putExtra("messageSMS","testBuzzcocks");
            startService(intent);
        }
    }
    private void initSpinnerWPM(){
        Spinner spinnerWPM = findViewById(R.id.spinnerWPM);
        String wpm = m.getWPM();
        Log.i("initSpinnerWPM ", wpm);
        String[] WPMoptions = getResources().getStringArray(R.array.WPMoptions);
        for (int i = 0; i < WPMoptions.length; i++) {
            if (WPMoptions[i].equals(m.getWPM())){
                Log.i("setting spinner", WPMoptions[i]);
                spinnerWPM.setSelection(i);
                break;
            }
        }
        spinnerWPM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // do something upon option selection
                m.setWPM(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
    public void vibratePat(View view){
        EditText editText = (EditText) findViewById(R.id.text1);
        String message;
        message = editText.getText().toString();
//        String msg="";
/*        for (int i=0;i<message.length();i++){
            msg +=message.charAt(i)+"\n";
        }*/
//        long[] pattern = m.assembler(message);
//        toast = Toast.makeText(getApplicationContext(), "vibrate start", Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER,0,0);
//        toast.show();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (0<=pattern[0]) {
//                VibrationEffect effect = VibrationEffect.createWaveform(pattern, -1);
//                vibrator.vibrate(effect);
//                Log.i("vibratePat", "vibrator");
//            }
            Intent intent = new Intent(getApplicationContext(),MorseVibrateService.class);
            intent.putExtra("messageSMS",message);
            startService(intent);
//        }
        Toast.makeText(getApplicationContext(),"message:\n"+ message, Toast.LENGTH_LONG).show();
    }
}
