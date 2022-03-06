package com.example.myapplication;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

public class AdvancedActivity extends AppCompatActivity {

    /**
     * Reference to this class used in {@link AdvancedActivity.Listener} methods
     */
    private AdvancedActivity advanced = this;
    /**
     * Holds the result data from activities launched from this activity
     */
    private Bundle resultData = null;

    private int openfileDialogId = 0;

    /**
     * @see Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);

        ((Button) findViewById(R.id.sslKeyBut)).setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                //showFileChooser();
                showDialog(openfileDialogId);
            }});

        ((CheckBox) findViewById(R.id.sslCheckBox)).setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked())
                {
                    ((Button)findViewById(R.id.sslKeyBut)).setClickable(true);
                }else
                {
                    ((Button)findViewById(R.id.sslKeyBut)).setClickable(false);
                }

            }});

        ((Button)findViewById(R.id.sslKeyBut)).setClickable(false);
    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_advanced, menu);

        Listener listener = new Listener();
        menu.findItem(R.id.setLastWill).setOnMenuItemClickListener(listener);
        menu.findItem(R.id.ok).setOnMenuItemClickListener(listener);

        return true;
    }

    /**
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        // get the last will data
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        resultData = intent.getExtras();

    }

    /**
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == openfileDialogId) {
            Map<String, Integer> images = new HashMap<String, Integer>();
            images.put(OpenFileDialog.sRoot, R.mipmap.ic_launcher);
            images.put(OpenFileDialog.sParent, R.mipmap.ic_launcher);
            images.put(OpenFileDialog.sFolder, R.mipmap.ic_launcher);
            images.put("bks", R.mipmap.ic_launcher);
            images.put(OpenFileDialog.sEmpty, R.mipmap.ic_launcher);
            Dialog dialog = OpenFileDialog.createDialog(id, this, "openfile",
                    new CallbackBundle() {
                        @Override
                        public void callback(Bundle bundle) {
                            String filepath = bundle.getString("path");
                            // setTitle(filepath);
                            ((EditText) findViewById(R.id.sslKeyLocaltion))
                                    .setText(filepath);
                        }
                    }, ".bks;", images);
            return dialog;
        }
        return null;
    }

    /**
     * Deals with button clicks for the advanced options page
     *
     */
    private class Listener implements OnMenuItemClickListener {

        /**
         * @see android.view.MenuItem.OnMenuItemClickListener#onMenuItemClick(MenuItem)
         */
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            int button = item.getItemId();

            switch (button) {
                case R.id.ok :
                    ok();
                    break;

                case R.id.setLastWill :
                    lastWill();
                    break;
            }
            return false;
        }

        /**
         * Packs the default options into an intent
         * @return intent packed with default options
         */
        @SuppressWarnings("unused")
        private Intent packDefaults() {
            Intent intent = new Intent();

            // check to see if there is any result data if there is not any
            // result data build some with defaults

            intent.putExtras(resultData);
            intent.putExtra(ActivityConstants.username, ActivityConstants.empty);
            intent.putExtra(ActivityConstants.password, ActivityConstants.empty);

            intent.putExtra(ActivityConstants.timeout, ActivityConstants.defaultTimeOut);
            intent.putExtra(ActivityConstants.keepalive,
                    ActivityConstants.defaultKeepAlive);
            intent.putExtra(ActivityConstants.ssl, ActivityConstants.defaultSsl);

            return intent;
        }

        /**
         *  Starts an activity to collect last will options
         */
        private void lastWill() {

            Intent intent = new Intent();
            intent.setClassName(advanced, "com.example.myapplication.LastWillActivity");
            advanced.startActivityForResult(intent, ActivityConstants.lastWill);

        }

        /**
         * Packs all the options the user has chosen, along with defaults the user has not chosen
         */
        private void ok() {

            int keepalive;
            int timeout;

            Intent intent = new Intent();

            if (resultData == null) {
                resultData = new Bundle();
                resultData.putString(ActivityConstants.message, ActivityConstants.empty);
                resultData.putString(ActivityConstants.topic, ActivityConstants.empty);
                resultData.putInt(ActivityConstants.qos, ActivityConstants.defaultQos);
                resultData.putBoolean(ActivityConstants.retained,
                        ActivityConstants.defaultRetained);
            }

            intent.putExtras(resultData);

            // get all advance options
            String username = ((EditText) findViewById(R.id.uname)).getText()
                    .toString();
            String password = ((EditText) findViewById(R.id.password))
                    .getText().toString();
            String sslkey = null;
            boolean ssl = ((CheckBox) findViewById(R.id.sslCheckBox)).isChecked();
            if(ssl)
            {
                sslkey = ((EditText) findViewById(R.id.sslKeyLocaltion))
                        .getText().toString();
            }
            try {
                timeout = Integer
                        .parseInt(((EditText) findViewById(R.id.timeout))
                                .getText().toString());
            }
            catch (NumberFormatException nfe) {
                timeout = ActivityConstants.defaultTimeOut;
            }
            try {
                keepalive = Integer
                        .parseInt(((EditText) findViewById(R.id.keepalive))
                                .getText().toString());
            }
            catch (NumberFormatException nfe) {
                keepalive = ActivityConstants.defaultKeepAlive;
            }

            //put the daya collected into the intent
            intent.putExtra(ActivityConstants.username, username);
            intent.putExtra(ActivityConstants.password, password);

            intent.putExtra(ActivityConstants.timeout, timeout);
            intent.putExtra(ActivityConstants.keepalive, keepalive);
            intent.putExtra(ActivityConstants.ssl, ssl);
            intent.putExtra(ActivityConstants.ssl_key, sslkey);
            //set the result as okay, with the data, and finish
            advanced.setResult(RESULT_OK, intent);
            advanced.finish();
        }
    }
}
