package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {
    TextView temperature;
    Button btn;
    Random Number;
    TextView Speed;
    int Rnumber;
    double Dwind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Speed = (TextView)findViewById(R.id.text_wind_spe);
        temperature = (TextView)findViewById(R.id.text_temperature);
        btn = (Button)findViewById(R.id.simpleSwitch);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Number = new Random();
                int Rnumber = ThreadLocalRandom.current().nextInt(10,22);
                temperature.setText(Integer.toString(Rnumber));

                double Swind = ThreadLocalRandom.current().nextDouble(2,3);
                //Speed.setText(Double.toString("value of Speed= %.1f",Swind));


            }
        });




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