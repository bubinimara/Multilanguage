package com.github.bubinimara.multilanguage.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.bubinimara.multilanguage.R;
import com.github.bubinimara.multilanguage.database.Session;
import com.github.bubinimara.multilanguage.network.HttpRequests;
import com.github.bubinimara.multilanguage.services.DatabaseIntentService;

public class SplashActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver receive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equalsIgnoreCase(DatabaseIntentService.ACTION_INIT_ERROR)){
                // threat error, can retry
                showDialog("Check your network connection and  that the server is up");
            }else if(action.equalsIgnoreCase(DatabaseIntentService.ACTION_INIT_SECCESS)){
                // go to the main activity
                startMainActivity();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Session.isInitialized(this)){
            startMainActivity();
            return;

        }

        setContentView(R.layout.activity_splash);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        doWork();
    }

    private void doWork(){
        DatabaseIntentService.startActionInit(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        localBroadcastManager.registerReceiver(receive, DatabaseIntentService.getIntentFilter());
    }

    @Override
    protected void onStop() {
        super.onStop();
        localBroadcastManager.unregisterReceiver(receive);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(R.string.dialog_button_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doWork();
                    }
                })
                .setNegativeButton(R.string.dialog_button_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SplashActivity.this.finish();
                    }
                })
                .show();
    }
}
