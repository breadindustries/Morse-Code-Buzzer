package com.breadIndustries.morseCodeBuzzer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class MorseSmsReceiver extends BroadcastReceiver {
    private static String TAG = "MorseSmsReceiver";
    public static final String pdu_type = "pdus";
//    reMorse m = new reMorse();
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        android.os.Debug.waitForDebugger();
        Log.e(TAG, "in broadcastReceiver");
//        boolean permSMS = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
        boolean permContacts = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;

        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String contact = "";
        String format = bundle.getString("format");
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                // Build the message to show.
//                strMessage += "SMS from ";
                if (permContacts) {
                    contact = getContactName(msgs[i].getOriginatingAddress(), context);
                        strMessage += contact;
                }
                else {
                    contact = msgs[i].getOriginatingAddress();
                    strMessage += contact;
                }
//                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                // Log and display the SMS message.
/*                long[] pattern = m.assembler(contact);
                if (0<=pattern[0]) {
                    VibrationEffect effect = VibrationEffect.createWaveform(pattern, -1);
                    vibrator.vibrate(effect);
                    Log.i("vibratePat", "vibrator");
                }*/
//                Log.i("sms receiver assembler", "sending " + strMessage + " to MVService");
                Intent intentSMS = new Intent(context,MorseVibrateService.class);
                intentSMS.putExtra("messageSMS",strMessage);
                context.startService(intentSMS);
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                break;
            }
        }
    }
    public String getContactName(final String phoneNumber, Context context) {
        final String TAG = "reMorse.getContactName";
        Log.i(TAG, "number: " + phoneNumber);
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }
        Log.i("contact query is null", String.valueOf(contactName.length()));
        if (contactName.length()<1){ contactName = phoneNumber;}
        return contactName;
    }
}
