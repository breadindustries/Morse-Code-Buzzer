package com.breadIndustries.morseCodeBuzzer;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LongSummaryStatistics;


public class reMorse {
    private final String TAG = "reMorse";
    private final long DIT = 1;
    private final long DAH = 3;
    private HashMap<String, String> map = new HashMap<>();
    private long WPM = 5;
    private long BASE = (long)((60.0 / (50.0 * WPM))*1000);
    protected reMorse(){
        setWPM("5");
    }
    protected reMorse(String message){
        buildMorseCodeTable();
    }
    private void buildMorseCodeTable() {
        map.put("A", "1,3");
        map.put("B", "3,1,1,1");
        map.put("C", "3,1,3,1");
        map.put("D", "3,1,1");
        map.put("E", "1");
        map.put("F", "1,1,3,1");
        map.put("G", "3,3,1");
        map.put("H", "1,1,1,1");
        map.put("I", "1,1");
        map.put("J", "1,3,3,3");
        map.put("K", "3,1,3");
        map.put("L", "1,3,1,1");
        map.put("M", "3,3");
        map.put("N", "3,1");
        map.put("O", "3,3,3");
        map.put("P", "1,3,3,1");
        map.put("Q", "3,3,1,3");
        map.put("R", "1,3,1");
        map.put("S", "1,1,1");
        map.put("T", "3");
        map.put("U", "1,1,3");
        map.put("V", "1,1,1,3");
        map.put("W", "1,3,3");
        map.put("X", "3,1,1,3");
        map.put("Y", "3,1,3,3");
        map.put("Z", "3,3,1,1");
        map.put("0", "3,3,3,3,3");
        map.put("1", "1,1,3,3,3");
        map.put("2", "1,1,3,3,3");
        map.put("3", "1,1,1,3,3");
        map.put("4", "1,1,1,1,3");
        map.put("5", "1,1,1,1,1");
        map.put("6", "3,1,1,1,1");
        map.put("7", "3,3,1,1,1");
        map.put("8", "3,3,3,1,1");
        map.put("9", "3,3,3,3,1");
    }

    public void setWPM(String wpm){
        this.WPM = Long.parseLong(wpm);
        this.BASE = (long)((60.0 / (50.0 * WPM))*1000);
        Log.i(TAG, "WPM " + WPM + " BASE " + BASE);
    }
    public String getWPM(){
        return String.valueOf(this.WPM);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)

    public long[] assembler(String strInput){
        Log.i(TAG, "assembler:"+strInput);
        ArrayList<Long> l = new ArrayList<>();
        l.add(0L);
        String[] words = strInput.trim().split("\\s+");
        for (int w=0;w<words.length;w++) {
            String word = words[w];
            Log.i(TAG, "word "+ word);
            for (int k = 0; k < word.length(); k++) {
                String get = map.get(String.valueOf(word.charAt(k)).toUpperCase());
                if (get != null) {
                    //working with the string returned from one character in string
                    //splitting and converting into long, then adding to arraylist
                    Log.i(TAG, "get string:" + get);
                    String[] elements = get.split(",");

                    for (int v = 0; v < elements.length; v++) {
                        Long e = Long.valueOf(elements[v]);
                        l.add(e * BASE);
                            l.add(BASE);
                    }
                    l.set(l.size() - 1, (3 * BASE));
                }
            }

            if (w == words.length - 1) {
                l.remove(l.size() - 1);
//                Log.i(TAG, "removing last break");
            }
            else{
//                Log.i(TAG,"Adding End of Word");
                l.set(l.size() - 1, (7 * BASE));
            }
        }

        long[] longVibratePattern = l.stream().mapToLong(Long::longValue).toArray();
        LongSummaryStatistics stats = l.stream().mapToLong(Long::longValue).summaryStatistics();
//        Log.i(TAG,"max:"+ stats.getMax());
        if (0== stats.getMax()){
            longVibratePattern = new long[]{-1};
        }

        Log.i(TAG, Arrays.toString(longVibratePattern));
        return longVibratePattern;
    }

}
