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
    Button button;
    IntentFilter intentfilter;
    float batteryTemp;
    String currentBatterytemp="Current Battery temp :";
    int batteryLevel;







    Random Number;
    Button btn;
    TextView temperature;
    TextView Speed;
    private TextView wholeView;
    private Handler updateHandler;
    int Rnumber;
    double Dwind;
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

        Thread t = new Thread() {

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
                                textViewTemp.setText(String.valueOf(Rnumber));

                                double Dwind = ThreadLocalRandom.current().nextDouble(2, 3);
                                textViewWind.setText(String.valueOf(Dwind));
                                textViewWind.setText(new DecimalFormat("##.##").format(Dwind));
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(5000);  //1000ms = 1 sec

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

        t.start();

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
//    Timer timeoutTimer;
//    final Random myRandom = new Random();
//    GenerateTask genTask = new GenerateTask();
//    final ArrayList<ArrayList<Integer>> arry1 = new ArrayList<ArrayList<Integer>>();
//    final ArrayList<ArrayList<Double>> arry2 = new ArrayList<ArrayList<Double>>();
//
//
//
//
//
//
//
//    class GenerateTask extends TimerTask {
//        boolean started = false;
//        @Override
//        public void run() {
//            if (started) {
//                System.out.println("generating");
//                final TextView textGenerateNumber = (TextView)findViewById(R.id.text_temperature);
//                arry1.clear();
//                for(int i=10;i<11;i++){
//                    ArrayList<Integer> Arry1 = new ArrayList<Integer>();
//                    Arry1.add(myRandom.nextInt(25));
//                    arry1.add(Arry1);
//                    arry1.remove("[");
//                };
//
//                final TextView textwind = (TextView)findViewById(R.id.text_wind_spe);
//                arry2.clear();
//                for (double k=2;k<3;k++){
//                    ArrayList<Double> Arry2 = new ArrayList<Double>();
//                    Arry2.add(myRandom.nextDouble());
//                    arry2.add(Arry2);
//
//
//
//                }
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        textGenerateNumber.setText(String.valueOf(arry1));
//                        textwind.setText(String.valueOf(arry2));
//                    }
//                });
//            }
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        Button buttonGenerate = (Button)findViewById(R.id.simpleSwitch);
//
//        buttonGenerate.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                System.out.println("click");
//                if (!genTask.started) {
//                    genTask.started=true;
//                    timeoutTimer = new Timer();
//                    timeoutTimer.scheduleAtFixedRate(genTask, 0, 5000);
//                } else {
//                    genTask.started=false;
//                    timeoutTimer.cancel();
//                }
//            }
//        });
//    }


//    TextView temperature;
//    Button btn;
//    Random Number;
//    TextView Speed;
//    int Rnumber;
//    double Dwind;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        Speed = (TextView) findViewById(R.id.text_wind_spe);
//        temperature = (TextView) findViewById(R.id.text_temperature);
//        btn = (Button) findViewById(R.id.simpleSwitch);
//
//
//        btn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Number = new Random();
//                int Rnumber = ThreadLocalRandom.current().nextInt(10, 22);
//                temperature.setText(Integer.toString(Rnumber));
//
//                double Swind = ThreadLocalRandom.current().nextDouble(2, 3);
//                Speed.setText(Double.toString(Swind));
//
//            }
//        });
//
//    }


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