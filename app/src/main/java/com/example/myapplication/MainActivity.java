package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView tempTextView;
    TextView temperature;
    TextView Speed;
    TextView wholeView;
    IntentFilter intentfilter;
    float batteryTemp;
    Random Number;
    Button btn;
    Handler updateHandler;
    String SDirection[] = {"Wind direction: North", "Wind direction: West", "Wind direction: N/W", "Wind direction: South", "Wind direction: N/S"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textViewTemp = (TextView) findViewById(R.id.text_temperature);
        TextView textViewWind = (TextView) findViewById(R.id.text_wind_spe);
        TextView textViewWindDirection = (TextView) findViewById(R.id.text_wind_dir);

        wholeView = findViewById(R.id.text_up_time);
        updateHandler = new Handler();
        updateUptimes();

        tempTextView = (TextView)findViewById(R.id.text_cpu_temperature);
        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        MainActivity.this.registerReceiver(broadcastreceiver,intentfilter);

        btn = (Button) findViewById(R.id.simpleSwitch);
        Speed = (TextView) findViewById(R.id.text_wind_spe);
        temperature = (TextView) findViewById(R.id.text_temperature);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        Thread tn = new Thread() {

            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(5000);  //1000ms = 1 sec
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Number = new Random();
                                int Rnumber = ThreadLocalRandom.current().nextInt(10, 22);
                                textViewTemp.setText(String.valueOf(Rnumber +" "+ (char) 0x00B0 +"C"));

                                double Dwind = ThreadLocalRandom.current().nextDouble(2, 3);
                                textViewWind.setText(String.valueOf(Dwind));
                                textViewWind.setText(new DecimalFormat("##.##").format(Dwind));
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        tn.start();

        Thread tx = new Thread() {

            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(500000);  //1000ms = 1 sec
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Random RDirection = new Random();
                                int index = RDirection.nextInt(SDirection.length - 0) + 0;
                                textViewWindDirection.setText(SDirection[index]);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        tx.start();
    }

    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            batteryTemp = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
            tempTextView.setText(batteryTemp +" "+ (char) 0x00B0 +"C");
        }
    };

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, Setting.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
