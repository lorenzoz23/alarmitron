package com.example.devon1.timetests;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            long milli = System.currentTimeMillis();
            long seconds = milli/1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            seconds = seconds - ((hours * 60) + (minutes * 60));
        while(1 == 1){
            if(milli <= System.currentTimeMillis()){
                seconds++;
                if(seconds >= 60){
                    minutes++;
                    seconds = 0;
                    if(minutes >= 60){
                        minutes = 0;
                        hours++;
                        if(hours >= 24){
                            hours = 0;
                        }
                    }
                }

            }
            System.out.println(hours + ":" + minutes + ":" + seconds);
        }
    }
}
