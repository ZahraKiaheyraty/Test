/*******************************************************************************
 * Copyright (c) 1999, 2014 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution. 
 *
 * The Eclipse Public License is available at 
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 */
package com.example.myapplication;

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

import java.util.HashMap;
import java.util.Map;

/**
 * AdvancedActivity connection options activity
 *
 */
public class AdvancedActivity extends AppCompatActivity {

  /**
   * Reference to this class used in {@link Listener} methods
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
   * @see Activity#onCreateOptionsMenu(Menu)
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
   * @see Activity#onOptionsItemSelected(MenuItem)
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
   * @see Activity#onActivityResult(int, int, Intent)
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
  	 * @see Activity#onCreateDialog(int)
  	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == openfileDialogId) {
			Map<String, Integer> images = new HashMap<String, Integer>();
			images.put(com.example.myapplication.OpenFileDialog.sRoot, R.mipmap.ic_launcher);
			images.put(com.example.myapplication.OpenFileDialog.sParent, R.mipmap.ic_launcher);
			images.put(com.example.myapplication.OpenFileDialog.sFolder, R.mipmap.ic_launcher);
			images.put("bks", R.mipmap.ic_launcher);
			images.put(com.example.myapplication.OpenFileDialog.sEmpty, R.mipmap.ic_launcher);
			Dialog dialog = com.example.myapplication.OpenFileDialog.createDialog(id, this, "openfile",
					new com.example.myapplication.CallbackBundle() {
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
     * @see OnMenuItemClickListener#onMenuItemClick(MenuItem)
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
      intent.putExtra(com.example.myapplication.ActivityConstants.username, com.example.myapplication.ActivityConstants.empty);
      intent.putExtra(com.example.myapplication.ActivityConstants.password, com.example.myapplication.ActivityConstants.empty);

      intent.putExtra(com.example.myapplication.ActivityConstants.timeout, com.example.myapplication.ActivityConstants.defaultTimeOut);
      intent.putExtra(com.example.myapplication.ActivityConstants.keepalive,
              com.example.myapplication.ActivityConstants.defaultKeepAlive);
      intent.putExtra(com.example.myapplication.ActivityConstants.ssl, com.example.myapplication.ActivityConstants.defaultSsl);

      return intent;
    }

    /**
     *  Starts an activity to collect last will options
     */
    private void lastWill() {

      Intent intent = new Intent();
      intent.setClassName(advanced, "io.bytehala.eclipsemqtt.sample.LastWillActivity");
      advanced.startActivityForResult(intent, com.example.myapplication.ActivityConstants.lastWill);

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
        resultData.putString(com.example.myapplication.ActivityConstants.message, com.example.myapplication.ActivityConstants.empty);
        resultData.putString(com.example.myapplication.ActivityConstants.topic, com.example.myapplication.ActivityConstants.empty);
        resultData.putInt(com.example.myapplication.ActivityConstants.qos, com.example.myapplication.ActivityConstants.defaultQos);
        resultData.putBoolean(com.example.myapplication.ActivityConstants.retained,
                com.example.myapplication.ActivityConstants.defaultRetained);
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
        timeout = com.example.myapplication.ActivityConstants.defaultTimeOut;
      }
      try {
        keepalive = Integer
            .parseInt(((EditText) findViewById(R.id.keepalive))
                .getText().toString());
      }
      catch (NumberFormatException nfe) {
        keepalive = com.example.myapplication.ActivityConstants.defaultKeepAlive;
      }

      //put the daya collected into the intent
      intent.putExtra(com.example.myapplication.ActivityConstants.username, username);
      intent.putExtra(com.example.myapplication.ActivityConstants.password, password);

      intent.putExtra(com.example.myapplication.ActivityConstants.timeout, timeout);
      intent.putExtra(com.example.myapplication.ActivityConstants.keepalive, keepalive);
      intent.putExtra(com.example.myapplication.ActivityConstants.ssl, ssl);
      intent.putExtra(com.example.myapplication.ActivityConstants.ssl_key, sslkey);
      //set the result as okay, with the data, and finish
      advanced.setResult(RESULT_OK, intent);
      advanced.finish();
    }

  }

}
