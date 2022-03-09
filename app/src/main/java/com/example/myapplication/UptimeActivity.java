package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UptimeActivity extends AppCompatActivity {

    private TextView wholeView;
    private Handler updateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wholeView = findViewById(R.id.text_up_time);
        updateHandler = new Handler();
        updateUptimes();

    }

    private void updateUptimes() {
        // Get the whole uptime
        long uptimeMillis = SystemClock.elapsedRealtime();
        String wholeUptime = String.format(Locale.getDefault(),
                "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(uptimeMillis),
                TimeUnit.MILLISECONDS.toMinutes(uptimeMillis)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                        .toHours(uptimeMillis)),
                TimeUnit.MILLISECONDS.toSeconds(uptimeMillis)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes(uptimeMillis)));
        wholeView.setText(wholeUptime);

        // Get the uptime without deep sleep
        long elapsedMillis = SystemClock.uptimeMillis();
        // Call updateUptimes after one second
        updateHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                updateUptimes();
            }
        }, 1000);
    }
}
