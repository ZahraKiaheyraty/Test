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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NavUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles collection of user information to create a new MQTT Client
 *
 */
public class NewConnectionActivity extends AppCompatActivity {

  /** {@link Bundle} which holds data from activities launched from this activity **/
  private Bundle result = null;

  /** 
   * @see android.app.Activity#onCreate(Bundle)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_connection);

    AppCompatButton fab = findViewById(R.id.connectButton);
    fab.setOnClickListener(view -> doConnectAction());

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
    adapter.addAll(readHosts());
    AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.serverURI);
    textView.setAdapter(adapter);

    //load auto compete options

  }

  /** 
   * @see android.app.Activity#onCreateOptionsMenu(Menu)
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_new_connection, menu);
    OnMenuItemClickListener listener = new Listener(this);
    menu.findItem(R.id.connectAction).setOnMenuItemClickListener(listener);
    menu.findItem(R.id.advanced).setOnMenuItemClickListener(listener);

    return true;
  }

  /** 
   * @see android.app.Activity#onOptionsItemSelected(MenuItem)
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
   * @see android.app.Activity#onActivityResult(int, int, Intent)
   */
  @SuppressLint("MissingSuperCall")
  @Override
  protected void onActivityResult(int requestCode, int resultCode,
      Intent intent) {

    if (resultCode == RESULT_CANCELED) {
      return;
    }

    result = intent.getExtras();

  }

  /**
   * Handles action bar actions
   *
   */
  private class Listener implements OnMenuItemClickListener {

    //used for starting activities 
    private NewConnectionActivity newConnection = null;

    public Listener(NewConnectionActivity newConnection)
    {
      this.newConnection = newConnection;
    }

    /**
     * @see OnMenuItemClickListener#onMenuItemClick(MenuItem)
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
      {
        // this will only connect need to package up and sent back

        int id = item.getItemId();

        Intent dataBundle = new Intent();

        switch (id) {
          case R.id.connectAction :
            doConnectAction();
            break;
          case R.id.advanced :
            //start the advanced options activity
            dataBundle.setClassName(newConnection,
                "com.example.myapplication.AdvancedActivity");
            newConnection.startActivityForResult(dataBundle,
                    com.example.myapplication.ActivityConstants.advancedConnect);

            break;
        }
        return false;

      }

    }

    /**
     * Add a server URI to the persisted file
     * 
     * @param serverURI the uri to store
     */
    private void persistServerURI(String serverURI) {
      File fileDir = newConnection.getFilesDir();
      File presited = new File(fileDir, "hosts.txt");
      BufferedWriter bfw = null;
      try {
        bfw = new BufferedWriter(new FileWriter(presited));
        bfw.write(serverURI);
        bfw.newLine();
      }
      catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      finally {
        try {
          if (bfw != null) {
            bfw.close();
          }
        }
        catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }

  }

  private void doConnectAction() {
    Intent dataBundle = new Intent();
    //extract client information
    String server = ((AutoCompleteTextView) findViewById(R.id.serverURI))
            .getText().toString();
    String port = ((EditText) findViewById(R.id.port))
            .getText().toString();
    String clientId = ((EditText) findViewById(R.id.clientId))
            .getText().toString();

    if (server.equals(com.example.myapplication.ActivityConstants.empty) || port.equals(com.example.myapplication.ActivityConstants.empty) || clientId.equals(com.example.myapplication.ActivityConstants.empty))
    {
      String notificationText = this.getString(R.string.missingOptions);
      com.example.myapplication.Notify.toast(this, notificationText, Toast.LENGTH_LONG);
//      return false;
    }

    boolean cleanSession = ((CheckBox) findViewById(R.id.cleanSessionCheckBox)).isChecked();
    //persist server
    persistServerURI(server);

    //put data into a bundle to be passed back to ClientConnections
    dataBundle.putExtra(com.example.myapplication.ActivityConstants.server, server);
    dataBundle.putExtra(com.example.myapplication.ActivityConstants.port, port);
    dataBundle.putExtra(com.example.myapplication.ActivityConstants.clientId, clientId);
    dataBundle.putExtra(com.example.myapplication.ActivityConstants.action, com.example.myapplication.ActivityConstants.connect);
    dataBundle.putExtra(com.example.myapplication.ActivityConstants.cleanSession, cleanSession);

    if (result == null) {
      // create a new bundle and put default advanced options into a bundle
      result = new Bundle();

      result.putString(com.example.myapplication.ActivityConstants.message,
              com.example.myapplication.ActivityConstants.empty);
      result.putString(com.example.myapplication.ActivityConstants.topic, com.example.myapplication.ActivityConstants.empty);
      result.putInt(com.example.myapplication.ActivityConstants.qos, com.example.myapplication.ActivityConstants.defaultQos);
      result.putBoolean(com.example.myapplication.ActivityConstants.retained,
              com.example.myapplication.ActivityConstants.defaultRetained);

      result.putString(com.example.myapplication.ActivityConstants.username,
              com.example.myapplication.ActivityConstants.empty);
      result.putString(com.example.myapplication.ActivityConstants.password,
              com.example.myapplication.ActivityConstants.empty);

      result.putInt(com.example.myapplication.ActivityConstants.timeout,
              com.example.myapplication.ActivityConstants.defaultTimeOut);
      result.putInt(com.example.myapplication.ActivityConstants.keepalive,
              com.example.myapplication.ActivityConstants.defaultKeepAlive);
      result.putBoolean(com.example.myapplication.ActivityConstants.ssl,
              com.example.myapplication.ActivityConstants.defaultSsl);

    }
    //add result bundle to the data being returned to ClientConnections
    dataBundle.putExtras(result);

    setResult(RESULT_OK, dataBundle);
    this.finish();
  }

  private void persistServerURI(String serverURI) {
    File fileDir = this.getFilesDir();
    File presited = new File(fileDir, "hosts.txt");
    BufferedWriter bfw = null;
    try {
      bfw = new BufferedWriter(new FileWriter(presited));
      bfw.write(serverURI);
      bfw.newLine();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally {
      try {
        if (bfw != null) {
          bfw.close();
        }
      }
      catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  /**
   * Read persisted hosts
   * @return The hosts contained in the persisted file
   */
  private String[] readHosts() {
    File fileDir = getFilesDir();
    File persisted = new File(fileDir, "hosts.txt");
    if (!persisted.exists()) {
      return new String[0];
    }
    ArrayList<String> hosts = new ArrayList<String>();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(persisted));
      String line = null;
      line = br.readLine();
      while (line != null) {
        hosts.add(line);
        line = br.readLine();
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      try {
        if (br != null) {
          br.close();
        }
      }
      catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return hosts.toArray(new String[hosts.size()]);

  }
}
